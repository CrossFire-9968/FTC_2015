package com.qualcomm.ftcrobotcontroller.opmodes;

import android.location.GpsSatellite;

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
      boolean Gp1_DPadRight = gamepad1.dpad_right;
      boolean Gp1_DPadLeft = gamepad1.dpad_left;
      // Change power multiplier based on trigger pressed
      if (Gp1_DPadUp == true)
      {
         PowerGain = 1.0f;
      }
      else if (Gp1_DPadDown == true)
      {
         PowerGain = 0.50f;
      }
      else if (Gp1_DPadRight == true)
      {
         PowerGain = 0.60f;
      }
      else if (Gp1_DPadLeft == true)
      {
         PowerGain = 0.40f;
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

}




