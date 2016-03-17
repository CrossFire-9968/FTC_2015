package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

/**
 * Created by Ryley on Mar/8/16.
 */
public class CF_BeaconTest extends CF_Hardware {
    ColorSensor sensorColor;
    public int red;
    public int green;
    public int blue;
    int state = 0;
    int subState = 0;

    public CF_BeaconTest() {}
        @Override public void start () {
            super.start();
        }
        @Override public void loop () {

            sensorColor = hardwareMap.colorSensor.get("color");
            /*
            red = sensorColor.red();
            green = sensorColor.green();
            blue = sensorColor.blue();

            if (red > redCutoff && green > greenCutoff && blue > blueCutoff) {
                allTrue = true;
            }
            else if (red < redCutoff && green < greenCutoff && blue < blueCutoff)
            {
                allTrue = false;
            }
            telemetry.addData("Red", red);
            telemetry.addData("Green", green);
            telemetry.addData("Blue", blue);
            telemetry.addData("red", sensorColor.red());
            telemetry.addData("allTrue", allTrue);*/


            switch (state) {
                case 0:
                    reset_drive_encoders();
                    run_using_encoders();
                    set_drive_power(-0.50f, -0.50f);
                    if(have_drive_encoders_reached(13500, 13500)) {
                        reset_drive_encoders();
                        set_drive_power(0.0f, 0.0f);
                        telemetry.addData("Encoders", "ENCODERS");
                        state++;
                    }
                    break;
                case 1:
                    reset_drive_encoders();
                    run_using_encoders();
                    set_drive_power(0.0f, -0.25f);
                    if(have_drive_encoders_reached(0, 2000)){
                        reset_drive_encoders();
                        set_drive_power(0.0f, 0.0f);
                        state++;
                    }
                    break;
                case 2:
                    red = 0;
                    green = 0;
                    blue = 0;
                    for(int i = 0; i < 10; i++) {
                        red = red + sensorColor.red();
                        green = green + sensorColor.green();
                        blue = blue + sensorColor.blue();
                    }
                    red = red / 10;
                    green = green / 10;
                    blue = blue / 10;
                    state++;
                    break;
                case 3:
                    if(red > green + 100 && red > blue + 100) {
                        BeaconState = BeaconState_E.RED;
                    }
                    if(blue > green + 100 && blue > red + 100){
                        BeaconState = BeaconState_E.BLUE;
                        telemetry.addData("BLUE1", "BLUE1");
                    }
                    state++;
                    break;
                case 4:
                    switch(BeaconState) {
                        case RED:
                            reset_drive_encoders();
                            run_using_encoders();
                            set_drive_power(0.25f, 0.25f);
                            if (have_drive_encoders_reached(500, 500)) {
                                reset_drive_encoders();
                                set_drive_power(0.0f, 0.0f);
                                telemetry.addData("RED", "RED");
                                state++;
                            }
                            break;
                        case BLUE:
                            switch(subState) {
                                case 0:
                                reset_drive_encoders();
                                run_using_encoders();
                                set_drive_power(-0.25f, -0.25f);
                                if (have_drive_encoders_reached(500, 500)) {
                                    reset_drive_encoders();
                                    set_drive_power(0.0f, 0.0f);
                                }
                                    subState++;
                                    break;
                                case 1:
                                run_using_encoders();
                                set_drive_power(0.25f, 0.10f);
                                if (have_drive_encoders_reached(300, 150)) {
                                    reset_drive_encoders();
                                    set_drive_power(0.0f, 0.0f);
                                }
                                    subState++;
                                    break;
                                case 2:
                                run_using_encoders();
                                set_drive_power(0.0f, 0.25f);
                                if (have_drive_encoders_reached(0, 300)) {
                                    reset_drive_encoders();
                                    set_drive_power(0.0f, 0.0f);

                                }
                                    subState++;
                                    break;
                                case 3:
                                for (int i = 0; i < 10; i++) {
                                    red = sensorColor.red() + red;
                                    green = sensorColor.green() + green;
                                    blue = sensorColor.blue() + blue;
                                }
                                red = red / 10;
                                green = green / 10;
                                blue = blue / 10;
                                if (blue > green + 100 && blue > red + 100) {
                                    BeaconState = BeaconState_E.BLUE;
                                } else if (red > green + 100 && red > blue + 100) {
                                    BeaconState = BeaconState_E.RED;
                                }
                                if (BeaconState == BeaconState_E.BLUE) {
                                    run_using_encoders();
                                    set_drive_power(0.25f, 0.25f);
                                    if (have_drive_encoders_reached(100, 100)) {
                                        reset_drive_encoders();
                                        set_drive_power(0.0f, 0.0f);
                                    }
                                }
                            }


                            state++;
                            break;
                        default:
                            telemetry.addData("OTHER", "OTHER");
                            break;
                    }
                break;
            }


            telemetry.addData("Red", red);
            telemetry.addData("Green", green);
            telemetry.addData("Blue", blue);
            telemetry.addData("Left Encoder", a_left_encoder_count());
            telemetry.addData("Right Encoder", a_right_encoder_count());
            telemetry.addData("State", state);
        }
    }
