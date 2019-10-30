package com.bogdan.persistentweb.errorhandling;

import org.eclipse.jetty.server.Request;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class ResourceNotFoundHandler {

  @RequestMapping("/resourceNotFound")
  public String noHandlerFoundForResource(final Request request) throws NoHandlerFoundException {
    throw new NoHandlerFoundException(request.getMethod(), request.getOriginalURI(), null);
  }
}
