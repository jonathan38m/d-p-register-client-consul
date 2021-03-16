package com.distribuida;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrincipalCliente {

    public static final String APP_NAME = "mp-registro";

    public static void main(String[] args) throws IOException {

        ConsulClient client = new ConsulClient("localhost");
        Response<Map<String, Service>> ss = client.getAgentServices();
        Map<String, Service> servicios = ss.getValue();


        servicios.values().stream().forEach(s -> {
            System.out.println(s.getId() + ":" + s.getService());
        });

        List<Service> servicioMp =servicios.values()
                .stream()
                .filter(s-> s.getService().equals(APP_NAME))
                .collect(Collectors.toList());


        System.out.println("--------------------------------------------------------------------");
        System.out.println("---- Lista de Servicios");


        CloseableHttpClient clienteHttp  = HttpClients.createDefault();

        for (Service s:servicioMp){
            String url = String.format("http://%s:%s/hola", s.getAddress(), s.getPort());
            System.out.println(s.getId() + "--->" + url);


            HttpGet get = new HttpGet(url);
            HttpResponse res = clienteHttp.execute(get);

            HttpEntity entity = res.getEntity();


            String ret = EntityUtils.toString(entity);

            System.out.println("        " + ret);
        }
        clienteHttp.close();

    }
}
