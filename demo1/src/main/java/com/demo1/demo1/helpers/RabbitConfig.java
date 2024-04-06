package com.demo1.demo1.helpers;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase encargada de la configuración de RabbitMQ
 * Configura el mensaje para que sea enviado en formato JSON
 * y se pueda enviar objetos de tipo StockMessage
 */
@Configuration
public class RabbitConfig {

    /**
     * Configura el mensaje para que sea enviado en formato JSON
     * ademas agrega a las clases de confianza la clase StockMessage
     * y los paquetes dentro de `helpers`
     * 
     * @return MessageConverter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        // classMapper.setTrustedPackages("com.demo1.demo1.helpers","com.buy_products.demo.helpers");
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);
        return converter;
    }

    /**
     * Configura el RabbitTemplate para que utilice el mensaje en formato JSON
     * al momentod e iniciar la conexion
     * 
     * @param connectionFactory La conexion a RabbitMQ
     * @return RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}