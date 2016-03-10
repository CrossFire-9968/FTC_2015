package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Jeffs_Hardware extends OpMode
{
   private DcMotor DriveMotor1;
   private DcMotor DriveMotor2;
   private Servo SpongeBobLeft;
   private Servo SpongeBobRight;
   private DcMotor ExtensionMotor;
   public DcMotor AimMotor;
   private Servo BucketServo;

   final static double OFF = 0.0f;
   final static double SpongeBobRightInitPosition = 0.65;
   final static double SpongeBobLeftInitPosition = 0.13;
   final static double BucketServoInitPosition = 0.82;

   final static double ServoLowerLimit = 0.0;
   final static double ServoUpperLimit = 1.0;
   final static double MotorLowerLimit = -1.0;
   final static double MotorUpperLimit = 1.0;

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
   public Jeffs_Hardware()
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
      SpongeBobLeft = hardwareMap.servo.get("SpongeBobLeft");
      SpongeBobRight = hardwareMap.servo.get("SpongeBobRight");
      ExtensionMotor = hardwareMap.dcMotor.get("ExtensionMotor");
      AimMotor = hardwareMap.dcMotor.get("AimMotor");
      BucketServo = hardwareMap.servo.get("BucketServo");

      // Reverse right side motors so left and right motors spin same direction on robot
      DriveMotor1.setDirection(DcMotor.Direction.FORWARD);
      DriveMotor2.setDirection(DcMotor.Direction.REVERSE);
      AimMotor.setDirection(DcMotor.Direction.REVERSE);
      ExtensionMotor.setDirection(DcMotor.Direction.FORWARD);

      SetSpongeBobRightPosition(SpongeBobRightInitPosition);
      SetSpongeBobLeftPosition(SpongeBobLeftInitPosition);
      SetBucketServoPosition(BucketServoInitPosition);
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
   // NAME: ScaleDriveMotorPower
   // DESC: Scale the drive motor to be more sensitive when the joystick
   //       position is near the center position to make more controllable at
   //       slower speeds
   //--------------------------------------------------------------------------
   public double ScaleDriveMotorPower(double powerInput)
   {
      double scaledPower;
      final int numPointsInMap = 16;

      // Ensure the values make sense.  Clip the values to max/min values
      double clippedPower = Range.clip(powerInput, MotorLowerLimit, MotorUpperLimit);

      // Array used to map joystick input to motor output
      double[] powerArray = {0.00, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30,
         0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

      // Get the corresponding index for the specified argument/parameter.
      int index = (int) (clippedPower * numPointsInMap);

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
   // NAME: ScaleWinchMotorPower
   // DESC: Scale the winch motor to be more sensitive when the joystick
   //       position is near the center position to make more controllable at
   //       slower speeds
   //--------------------------------------------------------------------------
   public double ScaleWinchMotorPower(double powerInput)
   {
      double scaledPower;
      final int numPointsInMap = 16;

      // Ensure the values make sense.  Clip the values to max/min values
      double clippedPower = Range.clip(powerInput, MotorLowerLimit, MotorUpperLimit);

      // Array used to map joystick input to motor output
      double[] powerArray = {0, 0.01, 0.02, 0.04, 0.05, 0.08, 0.11,
         0.13, 0.17, 0.23, 0.32, 0.4, 0.48, 0.61, 0.73, 0.89, 1};

      // Get the corresponding index for the specified argument/parameter.
      int index = (int) (clippedPower * numPointsInMap);

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
   // DESC: Get motor power level assigned to drive motor 1
   //--------------------------------------------------------------------------
   public double GetDriveMotorPower1()
   {
      double power = 0.0;

      if (DriveMotor1 != null)
      {
         power = DriveMotor1.getPower();
      }

      return power;
   }


   //--------------------------------------------------------------------------
   // NAME: GetDriveMotorPower2
   // DESC: Get motor power level assigned to drive motor 2
   //--------------------------------------------------------------------------
   public double GetDriveMotorPower2()
   {
      double power = 0.0;

      if (DriveMotor2 != null)
      {
         power = DriveMotor2.getPower();
      }

      return power;
   }


   //--------------------------------------------------------------------------
   // NAME: GetAimMotorPower
   // DESC: Get power level assigned to winch aiming motor
   //--------------------------------------------------------------------------
   double GetAimMotorPower()
   {
      double power = 0.0;

      if (AimMotor != null)
      {
         power = AimMotor.getPower();
      }

      return power;
   }

   //--------------------------------------------------------------------------
   // NAME: SetDriveConfig
   // DESC: Configure drive motor direction based on drive mode
   //--------------------------------------------------------------------------
   public void SetDriveConfig(DriveConfig_E value)
   {
      DriveConfig = value;

      // swap motor direction based on drive configuration
      switch (DriveConfig)
      {
         case MOUNTAIN:
            DriveMotor1.setDirection(DcMotor.Direction.REVERSE);
            DriveMotor2.setDirection(DcMotor.Direction.FORWARD);
            break;

         case DOZER:
         default:
            DriveMotor1.setDirection(DcMotor.Direction.FORWARD);
            DriveMotor2.setDirection(DcMotor.Direction.REVERSE);
            break;
      }
   }


   //--------------------------------------------------------------------------
   // NAME: SetDriveMotorPower
   // DESC: Assign and set drive motor power levels depending on drive mode
   //--------------------------------------------------------------------------
   public void SetDriveMotorPower(double powerLevel1, double powerLevel2)
   {
      double powerMotor1;
      double powerMotor2;

      // Switch which motor the power levels are assigned to.  This way when the robot
      // is driven in the reverse direction, the driver controls will still operate
      // in the same way.
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


   //region Winch Power
   //--------------------------------------------------------------------------
   // NAME: SetWinchPower
   // DESC: Set power level for the winch motors
   //--------------------------------------------------------------------------
   public void SetWinchPower(double extensionPower, double aimPower)
   {
      // Call private methods that individually handle the winch actuators
      SetWinchExtendPower(extensionPower);
      SetWinchAimPower(aimPower);
   }


   //--------------------------------------------------------------------------
   // NAME: SetWinchExtendPower
   // DESC: Set power level for the winch extend motor
   //--------------------------------------------------------------------------
   private void SetWinchExtendPower(double power)
   {
      if (ExtensionMotor != null)
      {
         ExtensionMotor.setPower(power);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: SetWinchAimPower
   // DESC: Set power level for the winch aim motor
   //--------------------------------------------------------------------------
   private void SetWinchAimPower(double power)
   {
      if (AimMotor != null)
      {
         AimMotor.setPower(power);
      }
   }
   //endregion


   //region SpongeBob
   //--------------------------------------------------------------------------
   // NAME: SetSpongeBobLeftPosition
   // DESC: Set position for lever arm attached to the left hand sponge bob
   //--------------------------------------------------------------------------
   public void SetSpongeBobLeftPosition(double servoPositionDesired)
   {
      // The entire rotation of the servo is scaled from 0 to 1.  This method
      // checks that the value is in this range and if not limits it to the
      // upper or lower limit.
      double servoPositionActual = Range.clip(servoPositionDesired, ServoLowerLimit, ServoUpperLimit);

      // Set servo power levels
      if (SpongeBobLeft != null)
      {
         SpongeBobLeft.setPosition(servoPositionActual);
      }
   }

   //--------------------------------------------------------------------------
   // NAME: SetSpongeBoRightPosition
   // DESC: Set position for lever arm attached to the right hand sponge bob
   //--------------------------------------------------------------------------
   public void SetSpongeBobRightPosition(double servoPositionDesired)
   {
      // The entire rotation of the servo is scaled from 0 to 1.  This method
      // checks that the value is in this range and if not limits it to the
      // upper or lower limit.
      double servoPositionActual = Range.clip(servoPositionDesired, ServoLowerLimit, ServoUpperLimit);

      // Set servo power levels
      if (SpongeBobRight != null)
      {
         SpongeBobRight.setPosition(servoPositionActual);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: GetSpongeBobLeftPosition
   // DESC:
   //--------------------------------------------------------------------------
   public double GetSpongeBobLeftPosition()
   {
      double position = 0.0;

      if (SpongeBobLeft != null)
      {
         position = SpongeBobLeft.getPosition();
      }
      return position;
   }

   //--------------------------------------------------------------------------
   // NAME: GetSpongeBobRightPosition
   // DESC:
   //--------------------------------------------------------------------------
   public double GetSpongeBobRightPosition()
   {
      double position = 0.0;

      if (SpongeBobRight != null)
      {
         position = SpongeBobRight.getPosition();
      }
      return position;
   }
   //endregion


   //--------------------------------------------------------------------------
   // NAME: SetBucketServoPosition
   // DESC:
   //--------------------------------------------------------------------------
   public void SetBucketServoPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, ServoLowerLimit, ServoUpperLimit);

      // Set servo power levels
      if (BucketServo != null)
      {
         BucketServo.setPosition(servoPositionActual);
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
      return position;
   }


   //--------------------------------------------------------------------------
   // NAME: RunUsingEncoderDriveMotor1
   // DESC: Set drive motor 1 encoder to run using encoder
   //--------------------------------------------------------------------------
   public void RunUsingEncoderDriveMotor1()
   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }
   }

   //--------------------------------------------------------------------------
   // NAME: RunUsingEncoderDriveMotor2
   // DESC: Set drive motor 2 encoder to run using encoder
   //--------------------------------------------------------------------------
   public void RunUsingEncoderDriveMotor2()
   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: RunUsingEncoderDriveMotor2
   // DESC: Set both drive wheel encoders to run, if the mode is appropriate
   //--------------------------------------------------------------------------
   public void RunUsingEncoders()
   {
      RunUsingEncoderDriveMotor1();
      RunUsingEncoderDriveMotor2();
   }


   //--------------------------------------------------------------------------
   // Method:  SetRunToPositionMode
   // Desc:    Set desired encoder position and set mode to RUN_TO_POSITION
   //--------------------------------------------------------------------------
   public void SetRunToPositionMode()
   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
      }

      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
      }
   }


   //--------------------------------------------------------------------------
   // Method:  SetDesiredDriveEncoderPositions
   // Desc:    Set desired encoder position and set mode to RUN_TO_POSITION
   //--------------------------------------------------------------------------
   public void SetDesiredDriveEncoderPositions(int driveMotor1Counts, int driveMotor2Counts)
   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setTargetPosition(driveMotor1Counts);
      }

      if (DriveMotor2 != null)
      {
         DriveMotor2.setTargetPosition(driveMotor2Counts);
      }
   }


   //--------------------------------------------------------------------------
   // Method:  Drive1EncodersReachedPosition
   // Desc:    Check to see if drive motor has reach desired position
   //--------------------------------------------------------------------------
   public boolean Drive1EncodersReachedPosition(int driveMotorCounts)
   {
      boolean positionReached = false;
      final int allowableError = 5;
      double encoderActual = Math.abs(DriveMotor1.getCurrentPosition());
      double encoderDesired = Math.abs(driveMotorCounts);

      if (DriveMotor1 != null)
      {
         if ((encoderActual > (encoderDesired - allowableError)) &&
             (encoderActual < (encoderDesired + allowableError)))
         {
            positionReached = true;
         }
      }

      return positionReached;
   }

   //--------------------------------------------------------------------------
   // Method:  Drive2EncodersReachedPosition
   // Desc:    Check to see if drive motor has reach desired position
   //--------------------------------------------------------------------------
   public boolean Drive2EncodersReachedPosition(int driveMotorCounts)
   {
      boolean positionReached = false;
      final int allowableError = 5;
      double encoderActual = Math.abs(DriveMotor2.getCurrentPosition());
      double encoderDesired = Math.abs(driveMotorCounts);

      if (DriveMotor2 != null)
      {
         if ((encoderActual > (encoderDesired - allowableError)) &&
            (encoderActual < (encoderDesired + allowableError)))
         {
            positionReached = true;
         }
      }

      return positionReached;
   }


   //--------------------------------------------------------------------------
   // Method:  GetDrive1MotorCounts
   // Desc:
   //--------------------------------------------------------------------------
   public int GetDrive1MotorCounts()
   {
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         encoderCounts = DriveMotor1.getCurrentPosition();
      }

      return encoderCounts;
   }


   //--------------------------------------------------------------------------
   // Method:  GetDrive2MotorCounts
   // Desc:
   //--------------------------------------------------------------------------
   public int GetDrive2MotorCounts()
   {
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         encoderCounts = DriveMotor2.getCurrentPosition();
      }

      return encoderCounts;
   }


   //--------------------------------------------------------------------------
   // Name: RunWithoutEncoderDriveMotor1
   // Desc: Set the left drive wheel encoder to run, if the mode is appropriate
   //--------------------------------------------------------------------------
   private void RunWithoutEncoderDriveMotor1()
   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }
   }


   //--------------------------------------------------------------------------
   // Name: RunWithoutEncoderDriveMotor2
   // Desc: Set the left drive wheel encoder to run, if the mode is appropriate
   //--------------------------------------------------------------------------
   private void RunWithoutEncoderDriveMotor2()

   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }
   }


   //--------------------------------------------------------------------------
   // Name: run_without_drive_encoders
   // Desc: Set both drive wheel encoders to run, if the mode is appropriate
   //--------------------------------------------------------------------------
   public void RunWithoutDriveEncoders()
   {
      RunWithoutEncoderDriveMotor1();
      RunWithoutEncoderDriveMotor2();
   }


   //--------------------------------------------------------------------------
   // Name: ResetEncoderDriveMotor1
   // Desc: Reset the left drive wheel encoder
   //--------------------------------------------------------------------------
   private void ResetEncoderDriveMotor1()
   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
      }
   }


   //--------------------------------------------------------------------------
   // Name: ResetEncoderDriveMotor2
   // Desc: Reset the right drive wheel encoder
   //--------------------------------------------------------------------------
   private void ResetEncoderDriveMotor2()
   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RESET_ENCODERS);
      }
   }


   //--------------------------------------------------------------------------
   // Name: ResetDriveEncoders
   // Desc: Reset both drive wheel encoders
   //--------------------------------------------------------------------------
   public void ResetDriveEncoders()
   {
      ResetEncoderDriveMotor1();
      ResetEncoderDriveMotor2();
   }


   //--------------------------------------------------------------------------
   // Name: EncoderCountsDriveMotor1
   // Desc: Get the encoder count of drive motor 1
   //--------------------------------------------------------------------------
   public int EncoderCountsDriveMotor1()
   {
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         encoderCounts = DriveMotor1.getCurrentPosition();
      }

      return encoderCounts;
   }


   //--------------------------------------------------------------------------
   // Name: EncoderCountsDriveMotor2
   // Desc: Get the encoder count of drive motor 2
   //--------------------------------------------------------------------------
   public int EncoderCountsDriveMotor2()
   {
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         encoderCounts = DriveMotor2.getCurrentPosition();
      }

      return encoderCounts;
   }


   //--------------------------------------------------------------------------
   // Name: HasDriveMotor1EncodersReachedPosition
   // Desc: Indicate whether drive motor 1 encoder has reached final position
   //--------------------------------------------------------------------------
   public boolean HasDriveMotor1EncodersReachedPosition(double p_count)
   {
      boolean positionReached = false;

      if (DriveMotor1 != null)
      {
         // Have the encoders reached the specified values?
         // TODO Implement stall code using these variables.
         if (Math.abs(DriveMotor1.getCurrentPosition()) > p_count)
         {
            positionReached = true;
         }
      }

      return positionReached;
   }


   //--------------------------------------------------------------------------
   // Name: HasDriveMotor2EncodersReachedPosition
   // Desc: Indicate whether drive motor 2 encoder has reached final position
   //--------------------------------------------------------------------------
   public boolean HasDriveMotor2EncodersReachedPosition(double p_count)
   {
      boolean positionReached = false;

      if (DriveMotor2 != null)
      {
         // Have the encoders reached the specified values?
         // TODO Implement stall code using these variables.
         if (Math.abs(DriveMotor2.getCurrentPosition()) > p_count)
         {
            positionReached = true;
         }
      }

      return positionReached;
   }


   //--------------------------------------------------------------------------
   // Name: drive_using_encoders
   // Desc: Indicate whether both drive motors encoders have reached final position
   //--------------------------------------------------------------------------
   public boolean HaveDriveMotorEncodersReachedPosition(double p_left_count, double p_right_count)
   {
      boolean positionReached = false;

      // Have encoders reached desired position
      boolean positionReachedDriveMotor1 = HasDriveMotor1EncodersReachedPosition(p_left_count);
      boolean positionReachedDriveMotor2 = HasDriveMotor2EncodersReachedPosition(p_right_count);

      // Have the encoders reached the specified values?
      if (positionReachedDriveMotor1 && positionReachedDriveMotor2)
      {
         positionReached = true;
      }

      return positionReached;
   }


   //--------------------------------------------------------------------------
   // Name: drive_using_encoders
   // Desc:
   //--------------------------------------------------------------------------
   public boolean DriveUsingEncoders(double p_left_power, double p_right_power, double p_left_count, double p_right_count)
   {
      // Assume the encoders have not reached the limit.
      boolean positionReached = false;

      // Tell the system that motor encoders will be used.
      RunUsingEncoders();

      // Start driving using desired drive wheel motor power levels
      SetDriveMotorPower(p_left_power, p_right_power);

      // Have the motor shafts turned the required amount?
      // If they haven't, then the motors will continue to run.
      if (HaveDriveMotorEncodersReachedPosition(p_left_count, p_right_count))
      {
         // Reset the encoders to ensure they are at a known good value.
         ResetDriveEncoders();

         // Stop the motors.
         SetDriveMotorPower(OFF, OFF);

         // Transition to the next state when this method is called again.
         positionReached = true;
      }

      return positionReached;
   }


   //--------------------------------------------------------------------------
   // Name: HasDriveMotor1EncoderReset
   // Desc: Indicate whether drive motor 1 encoder has been reset to zero
   //--------------------------------------------------------------------------
   private  boolean HasDriveMotor1EncoderReset()
   {
      boolean encoderHasReset = false;
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         // Get encoder counts for drive motor 1
         encoderCounts = EncoderCountsDriveMotor1();

         // Has the left encoder reached zero?
         if (encoderCounts == 0)
         {
            // Set the status to a positive indication.
            encoderHasReset = true;
         }
      }

      return encoderHasReset;
   }


   //--------------------------------------------------------------------------
   // Name: HasDriveMotor2EncoderReset
   // Desc: Indicate whether drive motor 2 encoder has been reset to zero
   //--------------------------------------------------------------------------
   private boolean HasDriveMotor2EncoderReset()
   {
      boolean encoderHasReset = false;
      int encoderCounts = 0;

      if (DriveMotor2 != null)
      {
         // Get encoder counts for drive motor 1
         encoderCounts = EncoderCountsDriveMotor2();

         // Has the left encoder reached zero?
         if (encoderCounts == 0)
         {
            // Set the status to a positive indication.
            encoderHasReset = true;
         }
      }

      return encoderHasReset;
   }


   //--------------------------------------------------------------------------
   // Name: HaveDriveMotorEncodersReset
   // Desc: Indicate whether drive motor encoders have been reset to zero
   //--------------------------------------------------------------------------
   public boolean HaveDriveMotorEncodersReset()
   {
      boolean encodersHaveReset = false;

      // Get actual encoder
      boolean encoderDriveMotor1HasReset = HasDriveMotor1EncoderReset();
      boolean encoderDriveMotor2HasReset = HasDriveMotor2EncoderReset();

      // Have the encoders reached zero?
      if (encoderDriveMotor1HasReset && encoderDriveMotor2HasReset)
      {
         encodersHaveReset = true;
      }

      return encodersHaveReset;
   }
}
