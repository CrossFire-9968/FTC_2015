package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;
//------------------------------------------------------------------------------
//
// PushBotAuto
//
/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author Ryley Hindman
 * @version 2015-08-01-06-01
 */
public class CF_AutonomousFinal extends CF_Hardware

{
    public int state = 1;
    private int v_state = 0;
    public CF_AutonomousFinal()

    {
    }

    @Override public void start ()

    {
        super.start();
        reset_drive_encoders();
        SetDriveConfig(DriveConfig_E.MOUNTAIN);

    }
    @Override public void loop ()

    {
        switch (v_state)
        {
            case 0:
                reset_drive_encoders();

                v_state++;

                break;
            case 1:

                run_using_encoders();

                set_drive_power(0.25f, 0.25f);

                if (have_drive_encoders_reached(5000, 5000))
                {
                    reset_drive_encoders();
                    //set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
//            case 2:
//                if (have_drive_encoders_reset())
//                {
//                    v_state++;
//                }
//                break;
//            case 3:
//                run_using_encoders();
//                set_drive_power(-0.25f, 0.25f);
//
//                if (have_drive_encoders_reached(2000, 2000))
//                {
//                    reset_drive_encoders();
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//
//                }
//
//                break;
//
//            case 4:
//                if (have_drive_encoders_reset())
//                {
//                    v_state++;
//                }
//                break;
//            case 5:
//                run_using_encoders();
//                set_drive_power(0.50f, 0.50f);
//
//                if (have_drive_encoders_reached(7000, 7000))
//                {
//                    reset_drive_encoders();
//
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//                }
//
//                break;
//            case 6:
//                if (have_drive_encoders_reset()) {
//                    v_state++;
//                }
//                break;
//            case 7:
//                run_using_encoders();
//                set_drive_power(-0.25f, -0.25f);
//                if(have_drive_encoders_reached(2000, 2000))
//                {
//                    reset_drive_encoders();
//
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//                }
//                break;
//            case 8:
//                if (have_drive_encoders_reset()) {
//                    v_state++;
//                }
//                break;
//            case 9:
//                run_using_encoders();
//                set_drive_power(0.25f, -0.25f);
//                if(have_drive_encoders_reached(1000, 1000)){
//                    reset_drive_encoders();
//
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//                }
//                break;
//            case 10:
//                if (have_drive_encoders_reset()) {
//                    v_state++;
//                }
//                break;
//            case 11:
//                run_using_encoders();
//                set_drive_power(0.25f, 0.25f);
//                if(have_drive_encoders_reached(500, 500)){
//                    reset_drive_encoders();
//
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//                }
//                break;
//            case 12:
//                if(have_drive_encoders_reset()) {
//                    v_state++;
//                }
//                break;
//            case 13:
//                SetBucketServoPosition(0.70);
//                SetBucketServoPosition(0.60);
//                SetBucketServoPosition(0.50);
//                SetBucketServoPosition(0.40);
//                long t = System.currentTimeMillis();
//                long t1 = System.currentTimeMillis();
//                while(t1 < t + 1000 ){
//                    t1 = System.currentTimeMillis();
//                    return;
//                }
//                SetBucketServoPosition(0.50);
//                SetBucketServoPosition(0.60);
//                SetBucketServoPosition(0.50);
//                SetBucketServoPosition(0.40);
//                SetBucketServoPosition(0.80);
//                v_state++;
//                break;
//            case 14:
//                run_using_encoders();
//                set_drive_power(-0.25f, -0.25f);
//                if(have_drive_encoders_reached(-7000, -7000)){
//                    reset_drive_encoders();
//
//                    set_drive_power(0.0f, 0.0f);
//                    v_state++;
//                }
//                break;




                default:
                    break;
        }
        int cfLeftCount = a_left_encoder_count();
        int cfRightCount = a_right_encoder_count();

        telemetry.addData ("18", "State: " + v_state);
        telemetry.addData("19", "Left Count: " + cfLeftCount);
        telemetry.addData("20", "Right Count: " + cfRightCount);
        telemetry.addData("21", "Left Drive Power: " + GetDriveMotorPower1());
        telemetry.addData("22", "Right Drive Power: " + GetDriveMotorPower2());
    }


} // PushBotAuto
