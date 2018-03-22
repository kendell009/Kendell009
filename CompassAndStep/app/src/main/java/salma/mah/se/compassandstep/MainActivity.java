package salma.mah.se.compassandstep;



        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.animation.Animation;
        import android.view.animation.RotateAnimation;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;


// Den app går inte rotera. Innan du kör appen, ska du vara säkert att du hade stängt av rotation på din mobil.

public class MainActivity extends AppCompatActivity {

    private EditText preesureValueET;
    private Button getMSL;
    private Button getBaroValue;
    private TextView baroTV;
    private TextView MSLTV;
    private ImageView compassIMG;
    private TextView gradeValue;
    private TextView stepCounter;
    private Button btnGetLowHeight;
    private Button btnGetHighHeight;
    private TextView TVGetLowHeight;
    private TextView TVGetHighHeight;
    private Button btnGetHeightRes;
    private TextView TVGetHeightRes;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor pressureSensor;
    private Sensor magnetSensor;
    private Sensor accelerometerSensor;
    private String pressureValue;
    private float height;
    private boolean isAccEnable = false;
    private boolean isMagEnable = false;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] MR = new float[9];
    private float[] orientationFloatValue = new float[3];
    private float oritationRadian;
    private float degress;
    private float currentDegree = 0f;
    private RotateAnimation rotateAnimation;
    // klass variable
    private StepCounterListner stepCounterListner;
    private PressureListner pressureListner;
    private CompassListner compassListner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hämtar de Sensorna som ska anvönder till app och dessutom anger också tillstånd för
        // att använda Sensor.
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //skapar alla textView och knapparna
        allConponentsCreated();
        //skapar alla Sensor inreKlass
        stepCounterListner = new StepCounterListner();
        pressureListner = new PressureListner();
        compassListner = new CompassListner();

    }

    // alla komponenter skapas här
    public void allConponentsCreated(){
        preesureValueET = (EditText)findViewById(R.id.editText_PressureValue);
        preesureValueET.setOnClickListener(new TextEditListner());
        getMSL = (Button)findViewById(R.id.button_hight);
        getMSL.setOnClickListener(new GetMSLButtonListner());
        getBaroValue = (Button)findViewById(R.id.button_baroValue);
        getBaroValue.setOnClickListener(new BaroButtonListner());
        baroTV = (TextView)findViewById(R.id.textView_baroValue);
        MSLTV = (TextView)findViewById(R.id.textView_MSLValue);
        compassIMG = (ImageView)findViewById(R.id.imageView_compass);
        gradeValue = (TextView)findViewById(R.id.textView_gradeValue);
        stepCounter = (TextView)findViewById(R.id.textView_stepCounter);
        btnGetLowHeight = (Button)findViewById(R.id.button_getLowHeight);
        btnGetHighHeight = (Button)findViewById(R.id.button_getHighHeight);
        btnGetLowHeight.setOnClickListener(new GetLowHeightButtonListner());
        btnGetHighHeight.setOnClickListener(new GetHighHeightButtonListner());
        TVGetLowHeight = (TextView)findViewById(R.id.textView_getLowHeight);
        TVGetHighHeight = (TextView)findViewById(R.id.textView_getHighHeight);
        btnGetHeightRes = (Button)findViewById(R.id.button_HeightRes);
        btnGetHeightRes.setOnClickListener(new GetHeightResButtonListner());
        TVGetHeightRes = (TextView)findViewById(R.id.textView_heightRes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Knapp lyssnare
    private class GetMSLButtonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            double coef = 1.0f / 5.255f;
            double p = Double.valueOf(pressureValue);
            double p0 = Double.valueOf(preesureValueET.getText().toString());
            height = 44330.0f * (1.0f - (float)Math.pow((p/p0), coef));
            //height = 44330.77*(1-(Math.pow(Double.valueOf(preesureValueET.getText().toString())/101.325,0.190263)));
            MSLTV.setText("Height:" +String.valueOf(height)+ "m");
            //  MSLTV.setText(("Height:"+String.valueOf(1.0/2.0)+"m"));
        }
    }

    // Knapp lyssnare
    private class BaroButtonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            baroTV.setText("Pressure: " + pressureValue + " kPa");
        }
    }

    // editText lyssnare
    private class TextEditListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            preesureValueET.setText("");
        }
    }

    // Knapp lyssnare räknar ut höjden när du är på 1:a våning
    private class GetLowHeightButtonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            double coef = 1.0f / 5.255f;
            double p = Double.valueOf(pressureValue);
            double p0 = Double.valueOf(preesureValueET.getText().toString());
            height = 44330.0f * (1.0f - (float)Math.pow((p/p0), coef));
            TVGetLowHeight.setText(String.valueOf(height));
        }
    }

    // Knapp lyssnare räknar ut höjden när du är på 7:d våning
    private class GetHighHeightButtonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            double coef = 1.0f / 5.255f;
            double p = Double.valueOf(pressureValue);
            double p0 = Double.valueOf(preesureValueET.getText().toString());
            height = 44330.0f * (1.0f - (float)Math.pow((p/p0), coef));
            TVGetHighHeight.setText(String.valueOf(height));
        }
    }

    // Knapp lyssnare här räknas ut skillnad mellan den lägsta höjden och högsta höjden
    private class GetHeightResButtonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            double lowHeight = Double.valueOf(TVGetLowHeight.getText().toString());
            double highHeight = Double.valueOf(TVGetHighHeight.getText().toString());
            TVGetHeightRes.setText("Just nu är du "+(highHeight - lowHeight)+"m ifrån havsytan.");
        }
    }

    // inre klass för tryckräkning
    private class PressureListner implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            float[] values = event.values;

            if(sensor.getType()==Sensor.TYPE_PRESSURE){
                pressureValue = String.valueOf(values[0]);

                // Log.i("MSL values",""+height);
                // Log.i("PRESSURE",pressureValue);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    // inre klass för stegräkning
    private class StepCounterListner implements SensorEventListener {

        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            float[] values = event.values;


            if(sensor.getType()==Sensor.TYPE_STEP_COUNTER){
                stepCounter.setText("StepCounter: "+String.valueOf(values[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
    // inre klass som tar hand om rörelse av kompassen
    private class CompassListner implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor==magnetSensor){
                isMagEnable = true;
                System.arraycopy(event.values,0,lastMagnetometer,0,event.values.length);
            }else if(event.sensor==accelerometerSensor){
                isAccEnable = true;
                System.arraycopy(event.values,0,lastAccelerometer,0,event.values.length);
            }else{
                Toast.makeText(getApplicationContext(),"Something its wrong!",Toast.LENGTH_LONG);
            }
            // om accelerometer och magnetfield är tillgänglig
            if(isAccEnable&&isMagEnable){
                SensorManager.getRotationMatrix(MR,null,lastAccelerometer,lastMagnetometer);
                SensorManager.getOrientation(MR, orientationFloatValue);
                oritationRadian = orientationFloatValue[0];
                // räkna ut hur mångar grader ska det bli när rotation ändras
                degress = (float)(Math.toDegrees(oritationRadian)+360)%360;
                rotateAnimation = new RotateAnimation(currentDegree,-degress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setDuration(300);
                //här ersätter bilden som ska roteras så fort den fick en value från sensor lyssnare.
                compassIMG.startAnimation(rotateAnimation);
                currentDegree = -degress;
                //visar hur mångar grader man får.
                gradeValue.setText(String.valueOf(degress));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    //När man stäng av mobilen, så ska alla sensor av registraras.
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(stepCounterListner, stepCounterSensor);
        sensorManager.unregisterListener(pressureListner, pressureSensor);
        sensorManager.unregisterListener(compassListner,magnetSensor);
        sensorManager.unregisterListener(compassListner,accelerometerSensor);
    }

    //När man roterar mobilen, så on resume alla sensor på en gång.
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(stepCounterListner, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(pressureListner,pressureSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassListner,magnetSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(compassListner,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

    }
}
