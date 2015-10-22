package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Extends the PushBotTelemetry and PushBotHardware classes to provide a basic
 * manual operational mode for the Push Bot.
 *
 * @author Jeff Dawson
 * @version 2015-08-01-06-01
 */
//------------------------------------------------------------------------------
public class FuzzyBotManual4x4 extends FuzzyBotHardware
{
//   private enum SteerType
//   {
//      singleStick, dualStick
//   }
//
//   ;
//   SteerType SteeringOption = SteerType.dualStick;

   //--------------------------------------------------------------------------
   // NAME: FuzzyBotManual4x4
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public FuzzyBotManual4x4()
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
      // check to see if steering strategy changed
//      this.TouchSensorChangeDriveStrategy();

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
      float Gp1_LeftStickY = -gamepad1.left_stick_y;
      float Gp1_RightStickY = -gamepad1.right_stick_y;
      float Gp1_RightStickX = gamepad1.right_stick_x;

      // Convert game pad values to meaningful motor power values.  X and Y
      // are range limited to +/-1.  Negative values are when joystick pushed
      // forward. DC motors are scaled to make it easier to control at slower speeds
      float LeftFrontDrivePower = (float) ScaleDriveMotorPower(Gp1_LeftStickY);
      float LeftRearDrivePower = (float) ScaleDriveMotorPower(Gp1_LeftStickY);
      float RightFrontDrivePower = (float) ScaleDriveMotorPower(Gp1_RightStickY);
      float RightRearDrivePower = (float) ScaleDriveMotorPower(Gp1_RightStickY);

      // Turn motors on
      SetDriveMotorPower(LeftFrontDrivePower, LeftRearDrivePower, RightFrontDrivePower, RightRearDrivePower);

      // Send motor telemetry data to the driver station
      telemetry.addData("01", "Left Front Drive: " + LeftFrontDriveMotorPower());
      telemetry.addData("02", "Left Rear Drive: " + LeftRearDriveMotorPower());
      telemetry.addData("03", "Right Front Drive: " + RightFrontDriveMotorPower());
      telemetry.addData("04", "Right Rear Drive: " + RightRearDriveMotorPower());
      telemetry.addData("10", "GP1 Left: " + Gp1_LeftStickY);
      telemetry.addData("11", "GP1 Right: " + Gp1_RightStickY);
   }


   //--------------------------------------------------------------------------
   // NAME: TouchSensorChangeDriveStrategy
   // DESC: Use touch sensor to change drive types for evaluation
   //--------------------------------------------------------------------------
//   public SteerType TouchSensorChangeDriveStrategy()
//   {
//      // Get a reference to the touch sensor
//      TouchSensor touchSensor = hardwareMap.touchSensor.get("sensor_touch");
//      boolean touchPressed = touchSensor.isPressed();
//
//      // If the touch sensor is pressed, toggle the steering type
//      if (touchPressed)
//      {
//         switch(SteeringOption)
//         {
//            case dualStick:
//               SteeringOption = SteerType.singleStick;
//               break;
//
//            case singleStick:
//            default:
//               SteeringOption = SteerType.dualStick;
//               break;
//         }
//      }
//
//      // Update telemetry data
//      if (SteeringOption == SteerType.dualStick)
//      {
//         telemetry.addData("05", "Steering Option: dualStick");
//      }
//      else
//      {
//         telemetry.addData("05", "Steering Option: dualStick");
//      }
//   }
}
