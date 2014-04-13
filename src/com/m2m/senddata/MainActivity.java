package com.m2m.senddata;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        
		Capteurs capteur = new Capteurs(
				(TextView)findViewById(R.id.xaccelero),
				(TextView)findViewById(R.id.yaccelero),
				(TextView)findViewById(R.id.zaccelero),
				(TextView)findViewById(R.id.xmagneto),
				(TextView)findViewById(R.id.ymagneto),
				(TextView)findViewById(R.id.zmagneto));
	
		super.onCreate(savedInstanceState);        

        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(capteur,
        		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        		SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(capteur,
        		sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
        		SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(capteur,
        		sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
        		SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(capteur,
        		sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
        		SensorManager.SENSOR_DELAY_NORMAL);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

class Capteurs implements SensorEventListener {

    public static final String BROKER_URL = "tcp://192.168.173.1:1883";
    public static final String TOPIC = "Test";
	private MqttClient client;
	private TextView xAcc, yAcc, zAcc,
					 xMagn, yMagn, zMagn;

    public Capteurs(TextView xA, TextView yA, TextView zA,
    				TextView xM, TextView yM, TextView zM) {
    	xAcc = xA;
    	yAcc = yA;
    	zAcc = zA;
    	xMagn = xM;
    	yMagn = yM;
    	zMagn = zM;


	    try {
			client = new MqttClient(BROKER_URL, MqttClient.generateClientId(), new MemoryPersistence());
	    	client.connect();
		} catch (MqttPersistenceException e) {
			System.out.println("Erreur de envois de message 1!");
			e.printStackTrace();
		} catch (MqttException e) {
			System.out.println("Erreur de envois de message 2!");
			e.printStackTrace();
		}
    }
    
	public void onSensorChanged(SensorEvent event) {
		String toSend = "";
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			xAcc.setText("X accelerometre: "+event.values[0]);
			yAcc.setText("Y accelerometre: "+event.values[1]);
			zAcc.setText("Z accelerometre: "+event.values[2]);
			toSend = "accex:" + event.values[0] + "\ry:" + event.values[1] + "\rz:" + event.values[2] + "\r";
		} else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
			xMagn.setText("X magnetometre: "+event.values[0]);
			yMagn.setText("Y magnetometre: "+event.values[1]);
			zMagn.setText("Z magnetometre: "+event.values[2]);
			toSend = "magnx:" + event.values[0] + "\ry:" + event.values[1] + "\rz:" + event.values[2] + "\r";
		}


        try {
        	final MqttTopic mqttTopic = client.getTopic(TOPIC);
            
    	    MqttMessage message = new MqttMessage(toSend.getBytes());
    	    mqttTopic.publish(message);
    	    System.out.println("Envois de : " + message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void onDestroy() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

};