package com.giulianobortolassi.mud.systems.weather;

import java.util.Random;

import com.giulianobortolassi.mud.Log;
import com.giulianobortolassi.mud.Server;
import com.giulianobortolassi.mud.systems.BaseSystem;

/**
 * WeatherControll.
 * 
 * Systems are statefull classes.
 */
public class WeatherControll implements BaseSystem {

    enum WeatherType {
        DRY(0), SUNNY(1), CLOUDED(2), RAINING(3), THUNDERSTORM(4);
    
        WeatherType(int index) { this.index = index; }
        int index;
    }

    private WeatherType currentWeather = WeatherType.SUNNY;

    private Random random;

    // We will change the weather every 2 minutes IRL. Each mud pulse is 1/10 per seccond, 
    // so we must check the weather every 10 * 60 * 2
    private final int WEATHER_PULSE = 10 * 30 * 1;

    @Override
    public void pulse(long pulse, Server server) {
        if( pulse % WEATHER_PULSE == 0 ){
            Log.info("Checking weather...", WeatherControll.class );
            random = new Random(System.nanoTime());

            // We have a 50% change to make weather better or worse.... so lets roll the dice  
            int currentIndex = currentWeather.index;
            int nextWeather = currentIndex;

            Log.info("CurrentWeather: [" + currentIndex + "]" , this.getClass());
            String weatherChange = "";
            if ( random.nextBoolean() ){
                // Weather get dry...
                nextWeather = --currentIndex < 0 ? 0 : currentIndex;               
                weatherChange = "dry";
            } else {
                nextWeather = ++currentIndex > WeatherType.values().length-1 ? WeatherType.values().length-1 : currentIndex;
                weatherChange = "humid";
            }

            Log.info("NextWeather: ["+ nextWeather + "]" , this.getClass());
            currentWeather = WeatherType.values()[nextWeather];
            
            String weatherMessage = String.format(" You fill the weathe getting %s ", weatherChange );
            Log.info("Changing Weather. Broadcasting: "+ weatherMessage + " CurrentWeather: " + currentWeather.toString(), this.getClass());


            // Call server global communications 
            server.broadcast(String.format(" %s \r\n", weatherMessage ));
        }
	}    
}