package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

//--------------------------------------------------------------------------
// NAME: FuzzyBotHardware
// DESC: Extends the OpMode class to provide a single hardware access point
//       for the Push Bot.
//--------------------------------------------------------------------------
public class FuzzyBotHardware extends OpMode
{
   private DcMotor LeftRearDriveMotor;                // Left rear motor
   private DcMotor RightRearDriveMotor;               // Right rear motor
   private DcMotor LeftFrontDriveMotor;               // Left front motor
   private DcMotor RightFrontDriveMotor;              // Right front motor


   //--------------------------------------------------------------------------
   // NAME: PushBotHardware
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public FuzzyBotHardware()
   {
      // Initialize base classes.  All via self-construction.
      // Initialize class members.  All via self-construction.
   }


   //--------------------------------------------------------------------------
   // NAME: Initialization method
   // DESC: Performs any actions that are necessary when the OpMode is enabled.
   //       The system calls this member once when the OpMode is enabled.
   //--------------------------------------------------------------------------
   @Override
   public void init()
   {
      // Use the hardwareMap to associate class members to hardware ports.
      // Note that the names of the devices (i.e. arguments to the get method)
      // must match the names specified in the configuration file created by
      // the FTC Robot Controller (Settings-->Configure Robot).
      LeftFrontDriveMotor = hardwareMap.dcMotor.get("LeftFrontDrive");
      LeftRearDriveMotor = hardwareMap.dcMotor.get("LeftRearDrive");
      RightFrontDriveMotor = hardwareMap.dcMotor.get("RightFrontDrive");
      RightRearDriveMotor = hardwareMap.dcMotor.get("RightRearDrive");

      // Reverse right side motors so left and right motors spin same direction on robot
      RightFrontDriveMotor.setDirection(DcMotor.Direction.REVERSE);
      RightRearDriveMotor.setDirection(DcMotor.Direction.REVERSE);
   }


   //--------------------------------------------------------------------------
   // NAME: start
   // DESC: Performs any actions that are necessary when the OpMode is enabled.
   //       The system calls this member once when the OpMode is enabled.
   //--------------------------------------------------------------------------
   @Override
   public void start()
   {
      // Only actions that are common to all Op-Modes (i.e. both automatic and
      // manual) should be implemented here.

      // This method is designed to be overridden.
   }


   //--------------------------------------------------------------------------
   // NAME: loop
   // DESC: Performs any actions that are necessary while the OpMode is running.
   // The system calls this member repeatedly while the OpMode is running.
   //--------------------------------------------------------------------------
   @Override
   public void loop()
   {
      // Only actions that are common to all OpModes (i.e. both auto and\
      // manual) should be implemented here.

      // This method is designed to be overridden.
   }


   //--------------------------------------------------------------------------
   // NAME: stop
   // DESC: Performs any actions that are necessary when the OpMode is disabled.
   //       The system calls this member once when the OpMode is disabled.
   //--------------------------------------------------------------------------
   @Override
   public void stop()
   {
      // Nothing needs to be done for this OpMode.
   }


   //--------------------------------------------------------------------------
   // NAME: scale_motor_power
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   double ScaleDriveMotorPower(double p_power)
   {
      // Assume no scaling.
      double l_scale = 0.0f;

      // Ensure the values are legal.
      double l_power = Range.clip(p_power, -1, 1);

      // Array used to map joystic input to motor output
      double[] l_array = {0.00, 0.05, 0.09, 0.10, 0.12,
         0.15, 0.18, 0.24, 0.30, 0.36,
         0.43, 0.50, 0.60, 0.72, 0.85,
         1.00, 1.00};

      // Get the corresponding index for the specified argument/parameter.
      int l_index = (int) (l_power * 16.0);

      // Limit the index to the number of table entries
      if (l_index < 0)
      {
         l_index = -l_index;
      }
      else if (l_index > 16)
      {
         l_index = 16;
      }

      // Handle negative power values as the table only had positive values
      if (l_power < 0)
      {
         l_scale = -l_array[l_index];
      }
      else
      {
         l_scale = l_array[l_index];
      }

      return l_scale;
   }


   //--------------------------------------------------------------------------
   // NAME: LeftFrontDriveMotorPower
   // DESC: Access the left front drive motor's power level.
   //--------------------------------------------------------------------------
   double LeftFrontDriveMotorPower()
   {
      return LeftFrontDriveMotor.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: LeftRearDriveMotorPower
   // DESC: Access the left rear drive motor's power level.
   //--------------------------------------------------------------------------
   double LeftRearDriveMotorPower()
   {
      return LeftRearDriveMotor.getPower();
   }


   // --------------------------------------------------------------------------
   // NAME: RightFrontDriveMotorPower
   // DESC: Access the right drive motor's power level.
   //--------------------------------------------------------------------------
   double RightFrontDriveMotorPower()
   {
      return RightFrontDriveMotor.getPower();
   }


   // --------------------------------------------------------------------------
   // NAME: RightDriveMotorPower
   // DESC: Access the right drive motor's power level.
   //--------------------------------------------------------------------------
   double RightRearDriveMotorPower()
   {
      return RightRearDriveMotor.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: SetDriveMotorPower
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   void SetDriveMotorPower(double leftFrontPower, double leftRearPower, double rightFrontPower, double rightRearPower)
   {
      // Set motor power levels
      LeftFrontDriveMotor.setPower(leftFrontPower);
      LeftRearDriveMotor.setPower(leftRearPower);
      RightFrontDriveMotor.setPower(rightFrontPower);
      RightRearDriveMotor.setPower(rightRearPower);
   }
}
