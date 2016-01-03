package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Lauren_FTC on 10/18/2015.
 */
public class CF_TracksManualRed extends CF_HardwareRed
{
   private float PowerGain = 0;

   //--------------------------------------------------------------------------
   // NAME: FuzzyBotManual4x4
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public CF_TracksManualRed()
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
      float Gp1_LeftStickY = gamepad1.left_stick_y;
      float Gp1_RightStickY = gamepad1.right_stick_y;
      boolean Gp1_DPadUp = gamepad1.dpad_up;
      boolean Gp1_DPadDown = gamepad1.dpad_down;
      boolean Gp1_DPadLeft = gamepad1.dpad_left;
      boolean Gp1_DPadRight = gamepad1.dpad_right;
      float Gp2_LeftStickY = gamepad2.left_stick_y;
//      float Gp2_RightStickX = gamepad2.right_stick_x;

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

      // Turn motors on
      SetDriveMotorPower(powerLevelDrive1, powerLevelDrive2);
//      SetBucketMotorPower(VerticalBucketMotorPower, HorizontalBucketMotorPower);
   }

//   private void bucketMotorControl()
//   {
//      float Gp1_LeftStickY = gamepad1.left_stick_y;
//      float Gp1_RightStickX = gamepad1.right_stick_x;
//      float VerticalBucketMotorPower = (float) ScaleDriveMotorPower(Gp2_LeftStickY);
//      float HorizontalBucketMotorPower = (float) ScaleDriveMotorPower(Gp2_RightStickX);
//   }

   //--------------------------------------------------------------------------
   // NAME: ZipLineServo
   // DESC: Method for operating servo to hit zip line triggers
   //--------------------------------------------------------------------------
   private void ServiceServos()
   {
      // Servo Motors Obtain the current values of the game pad 'RightBumper' and 'LeftBumper' buttons.
      // The clip method guarantees the value never exceeds the allowable
      // range.
      if (gamepad2.right_bumper)
      {
         SetZipLineServoPosition(GetZipLineServoPosition() + 0.005);
      }
      else if (gamepad2.left_bumper)
      {
         SetZipLineServoPosition(GetZipLineServoPosition() - 0.005);
      }

      if (gamepad2.dpad_down)
      {
         SetBucketServoPosition(GetBucketServoPosition() + 0.005);
      }
      else if (gamepad2.dpad_up)
      {
         SetBucketServoPosition(GetBucketServoPosition() - 0.003);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: UpdateTelemetry
   // DESC: Method for updating data displayed on phone
   //--------------------------------------------------------------------------
   public void UpdateTelemetry()
   {
      // Send telemetry data to the driver station.
      telemetry.addData("01", "version: v" + 0.6);
//      telemetry.addData("01", "Left Track Power: " + GetDrivePowerMotor1());
//      telemetry.addData("02", "Right Track Power: " + GetDriveMotorPower2());
//      telemetry.addData("03", "PowerGain: " + PowerGain);
      telemetry.addData("04", "ZipLineServo: " + GetZipLineServoPosition());
      switch (DriveConfig)
      {
         case MOUNTAIN:
            telemetry.addData("05", "Drive Mode: MOUNTAIN");
            break;

         case DOZER:
            telemetry.addData("05", "Drive Mode: DOZER");
            break;
      }
   }
}







