package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by jd72958 on 10/18/2015.
 */
public class CF_Hardware extends OpMode
{
   private DcMotor LeftTrackMotor;
   private DcMotor RightTrackMotor;
   private Servo ZipLineServo;
   private String WarningMessageString;
   private boolean WarningGenerated = false;

   //--------------------------------------------------------------------------
   // NAME: CF_Hardware
   // DESC: Constructor called when the class is instantiated.
   //--------------------------------------------------------------------------
   public CF_Hardware()
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
      LeftTrackMotor = hardwareMap.dcMotor.get("LeftTrackMotor");
      RightTrackMotor = hardwareMap.dcMotor.get("RightTrackMotor");
      ZipLineServo =  hardwareMap.servo.get("ZipLineServo");

      // Reverse right side motors so left and right motors spin same direction on robot
     RightTrackMotor.setDirection(DcMotor.Direction.REVERSE);

   }

   //--------------------------------------------------------------------------
   // NAME: DriveReverse
   // DESC: Changes drive motor direction
   //--------------------------------------------------------------------------
   public void DriveReverse()
   {
      LeftTrackMotor.setDirection(DcMotor.Direction.REVERSE);
      RightTrackMotor.setDirection(DcMotor.Direction.FORWARD);
   }

   //--------------------------------------------------------------------------
   // NAME: DriveForward
   // DESC: Changes drive motor direction
   //--------------------------------------------------------------------------
   public void DriveForward()
   {
      RightTrackMotor.setDirection(DcMotor.Direction.REVERSE);
      LeftTrackMotor.setDirection(DcMotor.Direction.FORWARD);
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

      // Array used to map joystick input to motor output
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
   // NAME: GetLeftTrackMotorPower
   // DESC: Access the left track motor's power level.
   //--------------------------------------------------------------------------
   double GetLeftTrackMotorPower()
   {
      return LeftTrackMotor.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: GetRightTrackMotorPower
   // DESC: Access the right track motor's power level.
   //--------------------------------------------------------------------------
   double GetRightTrackMotorPower()
   {
       return RightTrackMotor.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: SetMotorPower
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   void SetMotorPower(double leftMotorPower, double rightMotorPower)
   {
      // Set motor power levels
      if (LeftTrackMotor != null)
      {
         LeftTrackMotor.setPower(leftMotorPower);
      }

      if (RightTrackMotor != null)
      {
         RightTrackMotor.setPower(rightMotorPower);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: SetZipLineServoPosition
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   void SetZipLineServoPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, Servo.MIN_POSITION, Servo.MAX_POSITION);

      // Set servo power levels
      if (ZipLineServo != null)
      {
         try
         {
            ZipLineServo.setPosition(servoPositionActual);
         }

         catch (Exception p_exeception)
         {
            WarningMessage("ZipLineServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            ZipLineServo = null;
         }
      }
   }


   //--------------------------------------------------------------------------
   // NAME: GetZipLineServoPosition
   // DESC:
   //--------------------------------------------------------------------------
   double GetZipLineServoPosition()
   {
      double position = 0.0;

      if (ZipLineServo != null)
      {
         position = ZipLineServo.getPosition();
      }

      return position;
   }

   //--------------------------------------------------------------------------
   // NAME: WarningMessage
   // DESC:
   //--------------------------------------------------------------------------
   void WarningMessage (String exceptionMessage)
   {
      if (WarningGenerated)
      {
         WarningMessageString += ", ";
      }
      WarningGenerated = true;
      WarningMessageString += exceptionMessage;

   } // m_warning_message

}

