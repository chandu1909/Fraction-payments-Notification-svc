package com.fraction.notificationService.appConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author : Chandra sekhar Polavarapu
 * @Description: This provides all the configuration required for rabbit MQ. Although, we should take these values from the Application.properties.
 * To keep it simple, I have added them here.
 */
@Configuration
public class RabbitMqConfig {

        public static final String NOTIFICATIONS_QUEUE = "notifications_queue";
        public static final String FRACTION_MESSAGING_EXCHANGE = "fraction_messaging_exchange";
        public static final String FRACTION_CHANDRA_ROUTING_KEY = "fraction_chandra_routing_key";

        @Bean
        public Queue queue(){
            return new Queue (NOTIFICATIONS_QUEUE);

        }
        @Bean
        public TopicExchange exchange(){
            return new TopicExchange(FRACTION_MESSAGING_EXCHANGE);

        }
        @Bean
        public Binding binding(Queue queue, TopicExchange topicExchange){
            return BindingBuilder.bind(queue).to(topicExchange).with(FRACTION_CHANDRA_ROUTING_KEY);
        }

        @Bean
        public MessageConverter converter(){
            return new Jackson2JsonMessageConverter();
        }

        @Bean
        public AmqpTemplate template(ConnectionFactory connectionFactory){

            final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(converter());

            return rabbitTemplate;
        }
}
