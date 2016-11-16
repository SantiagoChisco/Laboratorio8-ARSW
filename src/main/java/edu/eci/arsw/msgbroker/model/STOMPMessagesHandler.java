/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.msgbroker.model;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author 2087052
 */
@Controller
public class STOMPMessagesHandler {

    @Autowired
    SimpMessagingTemplate msgt;
    @Autowired
    PointDriver pd;

    @MessageMapping("/newpoint")
    public void getLine(Point pt) throws Exception {
        System.out.println("Nuevo punto recibido en el servidor Apache:" + pt);
        pd.addPoint(pt);
        synchronized (pd.points) {
            if (pd.getPoints().size() >= 4) {
                List<Point> puntos = new LinkedList<>();
                puntos.add(pd.getPoints().remove());
                puntos.add(pd.getPoints().remove());
                puntos.add(pd.getPoints().remove());
                puntos.add(pd.getPoints().remove());
                msgt.convertAndSend("/topic/newpolygon", puntos);

            } else {
                msgt.convertAndSend("/topic/newpoint", pt);
            }
        }
    }

}
    

