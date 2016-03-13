package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Lauren_FTC on 10/18/2015.
 */
public class CF_TracksManual extends CF_Hardware
{
   private float PowerGain = 0;
   public int spongeBobState = -1;
   private static final float RightSpongeServoUpperLimit = 0.8f;
   private static final float RightSpongeServoLowerLimit = 0;
   private static final float LeftSpongeServoUpperLimit = 1.0f;
   private static final float LeftSpongeServoLowerLimit = 0;

   //--------------------------------------------------------------------------
   // NAME: FuzzyBotManual4x4
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public CF_TracksManual()
   {
      // Initialize base classes. All via self-construction.
      // Initialize class members. All via self-construction.
   }

   //--------------------------------------------------------------------------
   // NAME: loop
   // DESC: Initializes the class.  The system calls this member repeatedly
   //       while the OpMode is running.
   //--------------------------------------------------------------------------
   @Override
   public void loop()
   {
      // DC Motors obtain the current values of the joystick controllers.
      this.motorControl();

      // Method for operating ZipLineServo
      this.ServiceServos();

      this.UpdateTelemetry();
   }

   //--------------------------------------------------------------------------
   // NAME: motorControl
   // DESC: DC Motors obtain the current values of the joystick controllers.
   //       The setPower methods write the motor power values to the DcMotor
   //       class, but the power levels aren't applied until this method ends
   //--------------------------------------------------------------------------
   private void motorControl()
   {
      float powerLevelDrive1;
      float powerLevelDrive2;
      float powerLevelWinchE;
      float powerLevelWinchA;
      float Gp1_LeftStickY = gamepad1.left_stick_y;
      float Gp1_RightStickY = gamepad1.right_stick_y;
      boolean Gp1_DPadUp = gamepad1.dpad_up;
      boolean Gp1_DPadDown = gamepad1.dpad_down;
      float Gp2_RightStickY = gamepad2.right_stick_y;
      boolean Gp1_DPadLeft = gamepad1.dpad_left;
      boolean Gp1_DPadRight = gamepad1.dpad_right;
      float Gp2_LeftStickY = gamepad2.left_stick_y;


      // Change power multiplier based on D-Pad selection
      if (Gp1_DPadUp)
      {
         PowerGain = 1.00f;
      }
      else if (Gp1_DPadRight)
      {
         PowerGain = 0.60f;
      }
      else if (Gp1_DPadDown)
      {
         PowerGain = 0.45f;
      }
      else if (Gp1_DPadLeft)
      {
         PowerGain = 0.30f;
      }

      // Used to change driving direction
      if (gamepad1.a)
      {
         SetDriveConfig(DriveConfig_E.DOZER);
      }

      if (gamepad1.y)
      {
         SetDriveConfig(DriveConfig_E.MOUNTAIN);
      }

      // Convert game pad values to meaningful motor power values.  X and Y
      // are range limited to +/-1.  Negative values are when joystick pushed
      // forward. DC motors are scaled to make it easier to control at slower speeds
      powerLevelDrive1 = (float)ScaleDriveMotorPower(Gp1_LeftStickY) * PowerGain;
      powerLevelDrive2 = (float)ScaleDriveMotorPower(Gp1_RightStickY) * PowerGain;
      powerLevelWinchE = (float)ScaleWinchMotorPower(Gp2_RightStickY);

      if (Gp2_LeftStickY > 0)
      {
         powerLevelWinchA = (float)ScaleWinchMotorPower(Gp2_LeftStickY) * 0.20f;
      }
      else if (Gp2_LeftStickY < 0)
      {
         powerLevelWinchA = (float)ScaleWinchMotorPower(Gp2_LeftStickY) * 0.10f;
      }
      else
      {
         powerLevelWinchA = 0;
      }

      // Turn motors on
      SetDriveMotorPower(powerLevelDrive1, powerLevelDrive2);
      SetWinchPower(powerLevelWinchE, powerLevelWinchA);
   }

   //--------------------------------------------------------------------------
   // NAME: ZipLineServo
   // DESC: Method for operating servo to hit zip line triggers
   //--------------------------------------------------------------------------
   private void ServiceServos()
   {
      double SpongeLeftPosition = GetSpongeBobLeftPosition();
      double SpongeRightPosition = GetSpongeBobRightPosition();
      double BucketServoPosition = GetBucketServoPosition();

      //double ClawServoPosition = GetClawServoPosition();

      if (gamepad2.b)
      {
         // 0 = blue mountain
         spongeBobState = 0;
      }
      else if (gamepad2.x)
      {
         // 1 = red mountain
         spongeBobState = 1;
      }

      // Servo Motors Obtain the current values of the game pad 'RightBumper' and 'LeftBumper' buttons.
      // The clip method guarantees the value never exceeds the allowable
      // range.
      if(spongeBobState == 0)
      {
            if (gamepad2.left_bumper)
            {
               if (SpongeLeftPosition > LeftSpongeServoLowerLimit)
               {
                  SetSpongeBobLeftPosition(SpongeLeftPosition - 0.005);
               }
            }
            else if (gamepad2.right_bumper)
            {
               if (SpongeLeftPosition < LeftSpongeServoUpperLimit)
               {
                  SetSpongeBobLeftPosition(SpongeLeftPosition + 0.005);
               }
            }
      }
      else if(spongeBobState == 1)
      {
         if (gamepad2.right_bumper)
         {
            if (SpongeRightPosition < RightSpongeServoUpperLimit)
            {
               SetSpongeBobRightPosition(SpongeRightPosition + 0.005);
            }
         }
         else if (gamepad2.left_bumper)
         {
            if (SpongeRightPosition > RightSpongeServoLowerLimit)
            {
               SetSpongeBobRightPosition(SpongeRightPosition - 0.005);
            }
         }
      }

      if (gamepad2.dpad_up)
      {
         SetBucketServoPosition(BucketServoPosition - 0.003);
      }
      else if (gamepad2.dpad_down)
      {
         SetBucketServoPosition(BucketServoPosition + 0.005);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: UpdateTelemetry
   // DESC: Method for updating data displayed on phone
   //--------------------------------------------------------------------------
   public void UpdateTelemetry()
   {
      switch (DriveConfig)
      {
         case MOUNTAIN:
            telemetry.addData("01", "Drive Mode: MOUNTAIN");
            break;

         case DOZER:
            telemetry.addData("01", "Drive Mode: DOZER");
            break;
      }

   }
}







