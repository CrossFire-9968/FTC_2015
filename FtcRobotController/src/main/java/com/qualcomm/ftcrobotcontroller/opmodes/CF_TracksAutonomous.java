package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Lauren_FTC on 10/19/2015.
 */
public class CF_TracksAutonomous extends LinearOpMode
{
   private DcMotor LeftTrackMotor;
   private DcMotor RightTrackMotor;

   //--------------------------------------------------------------------------
   // NAME: CF_TracksAutonomous
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public CF_TracksAutonomous()
   {
     // Initialize base classes.  All via self-construction.
     // Initialize class members.  All via self-construction.
   }


   //--------------------------------------------------------------------------
   // NAME: runOpMode
   // DESC:
   //--------------------------------------------------------------------------
   @Override
   public void runOpMode() throws InterruptedException
   {
      LeftTrackMotor = hardwareMap.dcMotor.get("LeftTrackMotor");
      RightTrackMotor = hardwareMap.dcMotor.get("RightTrackMotor");
      RightTrackMotor.setDirection(DcMotor.Direction.REVERSE);

      waitForStart();

      // Turn on both track motors full power for 2000 msec
      LeftTrackMotor.setPower(1.0);
      RightTrackMotor.setPower(1.0);
      sleep(2000);

      // Tank turn for 500 msec
      LeftTrackMotor.setPower(-0.5);
      RightTrackMotor.setPower(0.5);
      sleep(500);

      // Drive forward for 1000 msec
      LeftTrackMotor.setPower(1.0);
      RightTrackMotor.setPower(1.0);
      sleep(1000);

      // Tank turn for 500 msec
      LeftTrackMotor.setPower(0.5);
      RightTrackMotor.setPower(-0.5);
      sleep(500);

      // Turn on both track motors full power for 2000 msec
      LeftTrackMotor.setPower(1.0);
      RightTrackMotor.setPower(1.0);
      sleep(2000);
   }
}


