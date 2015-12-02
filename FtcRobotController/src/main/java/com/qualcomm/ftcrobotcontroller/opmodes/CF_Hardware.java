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
   private DcMotor DriveMotor1;
   private DcMotor DriveMotor2;
   private Servo ZipLineServo;
   private Servo BucketServo;
   private String WarningMessageString;
   private boolean WarningGenerated = false;
//   private DcMotor VerticalBucketMotor;
//   private DcMotor HorizontalBucketMotor;

   public enum DriveConfig_E
   {
      MOUNTAIN,
      DOZER
   }

   public DriveConfig_E DriveConfig = DriveConfig_E.DOZER;

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
      DriveMotor1 = hardwareMap.dcMotor.get("DriveMotor1");
      DriveMotor2 = hardwareMap.dcMotor.get("DriveMotor2");
      ZipLineServo =  hardwareMap.servo.get("ZipLineServo");
      BucketServo = hardwareMap.servo.get ("BucketServo");

//      HorizontalBucketMotor = hardwareMap.dcMotor.get("HorizontalBucketMotor");
//      VerticalBucketMotor = hardwareMap.dcMotor.get("VerticalBucketMotor");

      // Reverse right side motors so left and right motors spin same direction on robot
      DriveMotor1.setDirection(DcMotor.Direction.FORWARD);
      DriveMotor2.setDirection(DcMotor.Direction.REVERSE);

      SetBucketServoPosition(1.0);
      SetZipLineServoPosition(1.0);
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
   public double ScaleDriveMotorPower(double powerInput)
   {
      double scaledPower = 0.0f;
      final int numPointsInMap = 16;

      // Ensure the values make sense.  Clip the values to max/min values
      double clippedPower = Range.clip(powerInput, -1, 1);

      // Array used to map joystick input to motor output
      double[] powerArray =  {0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
                              0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

      // Get the corresponding index for the specified argument/parameter.
      int index = (int)(clippedPower * numPointsInMap);

      // Array indexes can only be positive so we need to drop the negative
      if (index < 0)
      {
         index = -index;
      }

      // Limit indexes to actual size of array so we don't overflow
      if (index > numPointsInMap)
      {
         index = numPointsInMap;
      }

      // Handle negative power values as the table only had positive values
      if (clippedPower < 0)
      {
         scaledPower = -powerArray[index];
      }
      else
      {
         scaledPower = powerArray[index];
      }

      return scaledPower;
   }



   //--------------------------------------------------------------------------
   // NAME: GetDrivePowerMotor1
   // DESC: Access the left track motor's power level.
   //--------------------------------------------------------------------------
   public double GetDrivePowerMotor1()
   {
      return DriveMotor1.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: GetDriveMotorPower2
   // DESC: Access the right track motor's power level.
   //--------------------------------------------------------------------------
   public double GetDriveMotorPower2()
   {
       return DriveMotor2.getPower();
   }


   //--------------------------------------------------------------------------
   // NAME: GetHorizontalBucketMotorPower
   // DESC: Access the horizontal bucket motor's power level.
   //--------------------------------------------------------------------------
//   double GetHorizontalBucketMotorPower()
//   {
//      return HorizontalBucketMotor.getPower();
//   }
//
//   //--------------------------------------------------------------------------
//   // NAME: GetVerticalBucketMotorPower
//   // DESC: Access the horizontal bucket motor's power level.
//   //--------------------------------------------------------------------------
//   double GetVerticalalBucketMotorPower()
//   {
//      return VerticalBucketMotor.getPower();
//   }


   //--------------------------------------------------------------------------
   // NAME: SetDriveConfig
   // DESC: Set the drive configuration enum.  Used for switching between
   //       custom drive controls.
   //--------------------------------------------------------------------------
   public void SetDriveConfig(DriveConfig_E value)
   {
      DriveConfig = value;

      // swap motor direction based on drive configuration
      switch (DriveConfig)
      {
         case MOUNTAIN:
            DriveMotor1.setDirection(DcMotor.Direction.FORWARD);
            DriveMotor2.setDirection(DcMotor.Direction.REVERSE);
            break;

         case DOZER:
         default:
            DriveMotor1.setDirection(DcMotor.Direction.REVERSE);
            DriveMotor2.setDirection(DcMotor.Direction.FORWARD);
            break;
      }
   }


   //--------------------------------------------------------------------------
   // NAME: SetDriveMotorPower
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   public void SetDriveMotorPower(double powerLevel1, double powerLevel2)
   {
      double powerMotor1;
      double powerMotor2;

      // Assign motor power to appropriate side.
      switch (DriveConfig)
      {
         case MOUNTAIN:
            powerMotor1 = powerLevel1;
            powerMotor2 = powerLevel2;
            break;

         case DOZER:
         default:
            powerMotor1 = powerLevel2;
            powerMotor2 = powerLevel1;
            break;
      }

      // Set motor power levels
      if (DriveMotor1 != null)
      {
         DriveMotor1.setPower(powerMotor1);
      }

      if (DriveMotor2 != null)
      {
         DriveMotor2.setPower(powerMotor2);
      }
   }

   //--------------------------------------------------------------------------
   // NAME: SetBucketMotorPower
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
//   void SetBucketMotorPower(double VerticalBucketMotorPower, double HorizontalBucketMotorPower)
//   {
//      if (VerticalBucketMotor != null)
//      {
//         VerticalBucketMotor.setPower(VerticalBucketMotorPower);
//      }
//
//      if (HorizontalBucketMotor != null)
//      {
//         HorizontalBucketMotor.setPower(HorizontalBucketMotorPower);
//      }
//   }

   //--------------------------------------------------------------------------
   // NAME: SetZipLineServoPosition
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   public void SetZipLineServoPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, 0, 1);

      // Set servo power levels
      if (ZipLineServo != null)
      {
         try
         {
            ZipLineServo.setPosition(servoPositionActual);
            telemetry.addData("02", "servoPositionActual: " + servoPositionDesired);
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
   public double GetZipLineServoPosition()
   {
      double position = 0.0;

      if (ZipLineServo != null)
      {
         position = ZipLineServo.getPosition();
      }

      telemetry.addData("03", "position: " + ZipLineServo.getPosition());
      return position;
   }

   //--------------------------------------------------------------------------
   // NAME: SetBucketServoPosition
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   public void SetBucketServoPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, 0, 1);

      // Set servo power levels
      if (BucketServo != null)
      {
         try
         {
            BucketServo.setPosition(servoPositionActual);
            telemetry.addData("02", "servoPositionActual: " + servoPositionDesired);
         }

         catch (Exception p_exeception)
         {
            WarningMessage("BucketServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            BucketServo = null;
         }
      }
   }

   //--------------------------------------------------------------------------
   // NAME: GetBucketServoPosition
   // DESC:
   //--------------------------------------------------------------------------
   public double GetBucketServoPosition()
   {
      double position = 0.0;

      if (BucketServo != null)
      {
         position = BucketServo.getPosition();
      }

      telemetry.addData("03", "position: " + BucketServo.getPosition());
      return position;
   }

   //--------------------------------------------------------------------------
   // NAME: WarningMessage
   // DESC:
   //--------------------------------------------------------------------------
   public void WarningMessage (String exceptionMessage)
   {
      if (WarningGenerated)
      {
         WarningMessageString += ", ";
      }
      WarningGenerated = true;
      WarningMessageString += exceptionMessage;

   } // m_warning_message
}

