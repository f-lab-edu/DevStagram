package com.moondysmell.devstausers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@EnableRabbit
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    public static final String CHAT_QUEUE_NAME = "chat.queue";
    public static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    public static final String ROUTING_KEY = "room.*"; // * 한단어, # 0개이상상
    

    //Queue 등록
    @Bean
    public Queue queue(){
        return new Queue(CHAT_QUEUE_NAME, true);
        // mq서버가 죽더라도 queue와 메세지 손실 방지를 위해 durable설정
    }

    //Exchange 등록
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(CHAT_EXCHANGE_NAME);
    }

    //Exchange와 Queue 바인딩
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /* messageConverter를 커스터마이징 하기 위해 Bean 새로 등록 */
    //Producer
    @Bean
    public RabbitTemplate rabbitTemplate(){
        //Spring AMQP의 rabbitTemplate을 사용함으로써 메시지 발행
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
        return rabbitTemplate;
    }

    //consumer
     @Bean
     public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory){
         //POJO형태의 메시지를 소비
         SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
         container.setConnectionFactory(connectionFactory);
         container.setQueueNames(CHAT_QUEUE_NAME);
         container.setMessageListener(new MessageListener() {
             public void onMessage(Message message) {
                 System.out.println("Consuming Message - " + new String(message.getBody()));
             }
         });
         return container;
     }

//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver){
//        return new MessageListenerAdapter(receiver,"reveiveMessage");
//    }

    //Spring에서 자동생성해주는 ConnectionFactory는 SimpleConnectionFactory인데
    //여기서 사용하는 건 CachingConnectionFacotry라 새로 등록해줌
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        //LocalDateTime serializable을 위해
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.registerModule(dateTimeModule());

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        return converter;
    }

    @Bean
    public JavaTimeModule dateTimeModule(){
        return new JavaTimeModule();
    }

}
