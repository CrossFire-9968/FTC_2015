package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Lauren_FTC on 10/18/2015.
 */
public class CF_TracksManual extends CF_Hardware
{
   private float PowerGain = 0;

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
      this.ZipLineServo();
   }

   //--------------------------------------------------------------------------
   // NAME: motorControl
   // DESC: DC Motors obtain the current values of the joystick controllers.
   //       The setPower methods write the motor power values to the DcMotor
   //       class, but the power levels aren't applied until this method ends
   //--------------------------------------------------------------------------
   private void motorControl()
   {
      // Invert sign of motor values from game pads
      float Gp1_LeftStickY = gamepad1.left_stick_y;
      float Gp1_RightStickY = gamepad1.right_stick_y;
      boolean Gp1_DPadUp = gamepad1.dpad_up;
      boolean Gp1_DPadDown = gamepad1.dpad_down;
      boolean Gp1_DPadLeft = gamepad1.dpad_left;
      boolean Gp1_DPadRight = gamepad1.dpad_right;

      // Change power multiplier based on trigger pressed
      if (Gp1_DPadUp == true)
      {
         PowerGain = 0.10f;
      }
      else if (Gp1_DPadRight == true)
      {
         PowerGain = 0.20f;
      }
      else if (Gp1_DPadDown == true)
      {
         PowerGain = 0.30f;
      }
      else if (Gp1_DPadLeft == true)
      {
         PowerGain = 0.40f;
      }

      // Used to change driving direction
      if (gamepad1.a)
      {
         DriveReverse();
      }

      if (gamepad1.y)
      {
         DriveForward();
      }

      // Convert game pad values to meaningful motor power values.  X and Y
      // are range limited to +/-1.  Negative values are when joystick pushed
      // forward. DC motors are scaled to make it easier to control at slower speeds
      float LeftTrackMotorPower = (float) ScaleDriveMotorPower(Gp1_LeftStickY) * PowerGain;
      float RightTrackMotorPower = (float) ScaleDriveMotorPower(Gp1_RightStickY) * PowerGain;

      // Turn motors on
      SetMotorPower(LeftTrackMotorPower, RightTrackMotorPower);

      // Send motor telemetry data to the driver station
      telemetry.addData("01", "Left Track Power: " + GetLeftTrackMotorPower());
      telemetry.addData("02", "Right Track Power: " + GetRightTrackMotorPower());
      telemetry.addData("10", "GP1 Left: " + Gp1_LeftStickY);
      telemetry.addData("11", "GP1 Right: " + Gp1_RightStickY);
      telemetry.addData("03", "PowerGain: " + PowerGain);

   }

   private void bucketMotorControl()
   {
      float Gp1_LeftStickY = gamepad1.left_stick_y;
      float Gp1_RightStickX = gamepad1.right_stick_x;
   }

   //--------------------------------------------------------------------------
   // NAME: ZipLineServo
   // DESC: Method for operating servo to hit zip line triggers
   //--------------------------------------------------------------------------
   private void ZipLineServo()
   {
      // Servo Motors Obtain the current values of the gamepad 'RightBumper' and 'LeftBumper' buttons.
      // The clip method guarantees the value never exceeds the allowable
      // range.
      if (gamepad1.right_bumper)
      {
         SetZipLineServoPosition(GetZipLineServoPosition() + 0.05);
      }
      else if (gamepad1.left_bumper)
      {
         SetZipLineServoPosition(GetZipLineServoPosition() - 0.05);
      }

      // Send telemetry data to the driver station.
      telemetry.addData("04", "Zip Line Servo: " + GetZipLineServoPosition());
   }
}







