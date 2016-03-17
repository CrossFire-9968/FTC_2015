package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by jd72958 on 10/18/2015.
 */
public class CF_Hardware extends OpMode
{
   private DcMotor DriveMotor1;
   private DcMotor DriveMotor2;
   private Servo SpongeBobLeft;
   private Servo SpongeBobRight;
   private DcMotor ExtensionMotor;
   public DcMotor AimMotor;
   private Servo BucketServo;
   private DcMotor ReaperMotor;

   public enum DriveConfig_E
   {
      MOUNTAIN,
      DOZER
   }

   public DriveConfig_E DriveConfig = DriveConfig_E.DOZER;

   public enum BeaconState_E{
      RED,
      BLUE,
      NULL
   }
   public BeaconState_E BeaconState = BeaconState_E.NULL;

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
      SpongeBobLeft = hardwareMap.servo.get("SpongeBobLeft");
      SpongeBobRight = hardwareMap.servo.get("SpongeBobRight");
      ExtensionMotor = hardwareMap.dcMotor.get("ExtensionMotor");
      AimMotor = hardwareMap.dcMotor.get("AimMotor");
      BucketServo = hardwareMap.servo.get("BucketServo");
      //TouchSensor1 = hardwareMap.touchSensor.get ("TouchSensor1");
      ReaperMotor = hardwareMap.dcMotor.get("ReaperMotor");

      // Reverse right side motors so left and right motors spin same direction on robot
      DriveMotor1.setDirection(DcMotor.Direction.FORWARD);
      DriveMotor2.setDirection(DcMotor.Direction.REVERSE);
      AimMotor.setDirection(DcMotor.Direction.REVERSE);
      ExtensionMotor.setDirection(DcMotor.Direction.FORWARD);
      ReaperMotor.setDirection(DcMotor.Direction.FORWARD);

      SetSpongeBobRightPosition(0.65);
      SetSpongeBobLeftPosition(0.13);
      SetBucketServoPosition(0.82);
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
      double scaledPower;
      final int numPointsInMap = 16;

      // Ensure the values make sense.  Clip the values to max/min values
      double clippedPower = Range.clip(powerInput, -1, 1);

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

   public double ScaleWinchMotorPower(double powerInput)
   {
      double scaledPower;
      final int numPointsInMap = 16;

      // Ensure the values make sense.  Clip the values to max/min values
      double clippedPower = Range.clip(powerInput, -1, 1);

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
   // DESC: Access the left track motor's power level.
   //--------------------------------------------------------------------------
   public double GetDriveMotorPower1()
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
   // NAME: GetDriveMotorPower2
   // DESC: Access the right track motor's power level.
   //--------------------------------------------------------------------------
   double GetAimMotorPower()
   {
      return AimMotor.getPower();
   }

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
   // NAME:
   // DESC:
   //--------------------------------------------------------------------------
   public void SetWinchPower(double powerLevel1, double powerLevel2)
{
   // Set motor power levels
   if (ExtensionMotor != null)
   {
      ExtensionMotor.setPower(powerLevel1);
   }

   if (AimMotor != null)
   {
      AimMotor.setPower(powerLevel2);
   }
}


   //--------------------------------------------------------------------------
   // NAME: SetReaperMotorPower
   // DESC:
   //--------------------------------------------------------------------------
   public void SetReaperMotorPower(double power)
   {
      // Set reaper motor power level
      if (ReaperMotor != null)
      {
         ReaperMotor.setPower(power);
      }
   }


   //--------------------------------------------------------------------------
   // NAME: SetSpongeBobLeftPosition
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   public void SetSpongeBobLeftPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, 0, 1);

      // Set servo power levels
      if (SpongeBobLeft != null)
      {
         try
         {
            SpongeBobLeft.setPosition(servoPositionActual);
         }

         catch (Exception p_exeception)
         {
//            WarningMessage("ZipLineServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            SpongeBobLeft = null;
         }
      }
   }

   //--------------------------------------------------------------------------
   // NAME: SetSpongeBoRightPosition
   // DESC: Scale the joystick input using a nonlinear algorithm.
   //--------------------------------------------------------------------------
   public void SetSpongeBobRightPosition(double servoPositionDesired)
   {
      // Ensure the specific value is legal.
      double servoPositionActual = Range.clip(servoPositionDesired, 0, 1);

      // Set servo power levels
      if (SpongeBobRight != null)
      {
         try
         {
            SpongeBobRight.setPosition(servoPositionActual);
         }

         catch (Exception p_exeception)
         {
//            WarningMessage("ZipLineServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            SpongeBobRight = null;
         }
      }
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
         }

         catch (Exception p_exeception)
         {
//            WarningMessage("ZipLineServo");
            DbgLog.msg(p_exeception.getLocalizedMessage());
            BucketServo = null;
         }
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

   //------------------------------------------------------------
   //------------------------------------------------------------
   // This is the starting of the drive encoder definitions
   //used by PushBotAuto.java
   // Added: 4/11/2015 by Ryley Hindman(jumbojet0105)
   //------------------------------------------------------------
   //------------------------------------------------------------

   // So basically, this method brings the motor encoders back
   //into sync with each other, thus helping ensure that the
   //tracks end up at the same spot at the same time.
   // Feel free to play with the power settings, because these
   //are just arbitrary settings I chose.
   // Also, I'm not sure if a_..._encoder_count(); is the right
   //method to use, but I guess we'll find out...
   public void cf_realign_encoders()
   {
      int cfLeft = a_left_encoder_count();
      int cfRight = a_right_encoder_count();

      if (cfLeft > cfRight)
      {
         set_drive_power(0.95f, 1.0f);
      }
      else if (cfRight > cfLeft)
      {
         set_drive_power(1.0f, 0.95f);
      }
      else if (cfRight == cfLeft)
      {
         set_drive_power(0.0f, 0.0f);
      }
   }
   // cf_relalign_encoders


   void set_drive_power(double p_left_power, double p_right_power)

   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setPower(p_left_power);
      }
      if (DriveMotor2 != null)
      {
         DriveMotor2.setPower(p_right_power);
      }

   } // set_drive_power

   //--------------------------------------------------------------------------
   //
   // run_using_left_drive_encoder
   //

   /**
    * Set the left drive wheel encoder to run, if the mode is appropriate.
    */
   public void run_using_left_drive_encoder()

   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }

   } // run_using_left_drive_encoder

   //--------------------------------------------------------------------------
   //
   // run_using_right_drive_encoder
   //

   /**
    * Set the right drive wheel encoder to run, if the mode is appropriate.
    */
   public void run_using_right_drive_encoder()

   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }

   } // run_using_right_drive_encoder

   //--------------------------------------------------------------------------
   //
   // run_using_encoders
   //

   /* Set both drive wheel encoders to run, if the mode is appropriate.
   */
   public void run_using_encoders()

   {
      //
      // Call other members to perform the action on both motors.
      //
      run_using_left_drive_encoder();
      run_using_right_drive_encoder();

   } // run_using_encoders


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
   public boolean Drive1EncodersReachedPosition(int driveMotor1Counts)
   {
      boolean positionReached = false;

      if (DriveMotor1 != null)
      {
         if (Math.abs(DriveMotor1.getCurrentPosition()) > Math.abs(driveMotor1Counts))
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
   public boolean Drive2EncodersReachedPosition(int driveMotor2Counts)
   {
      boolean positionReached = false;

      if (DriveMotor2 != null)
      {
         if (Math.abs(DriveMotor2.getCurrentPosition()) > Math.abs(driveMotor2Counts))
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
   int GetDrive1MotorCounts()
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
   int GetDrive2MotorCounts()
   {
      int encoderCounts = 0;

      if (DriveMotor1 != null)
      {
         encoderCounts = DriveMotor2.getCurrentPosition();
      }

      return encoderCounts;
   }


   //--------------------------------------------------------------------------

   /**
   //
   // run_without_left_drive_encoder
   //

   /**
    * Set the left drive wheel encoder to run, if the mode is appropriate.
    */
   public void run_without_left_drive_encoder()

   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }

   } // run_without_left_drive_encoder

   //--------------------------------------------------------------------------
   //
   // run_without_right_drive_encoder
   //

   /**
    * Set the right drive wheel encoder to run, if the mode is appropriate.
    */
   public void run_without_right_drive_encoder()

   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }

   } // run_without_right_drive_encoder

   //--------------------------------------------------------------------------
   //
   // run_without_drive_encoders
   //

   /**
    * Set both drive wheel encoders to run, if the mode is appropriate.
    */
   public void run_without_drive_encoders()
   {
      //
      // Call other members to perform the action on both motors.
      //
      run_without_left_drive_encoder();
      run_without_right_drive_encoder();

   } // run_without_drive_encoders

   //--------------------------------------------------------------------------
   //
   // reset_left_drive_encoder
   //

   /**
    * Reset the left drive wheel encoder.
    */
   public void reset_left_drive_encoder()

   {
      if (DriveMotor1 != null)
      {
         DriveMotor1.setMode(DcMotorController.RunMode.RESET_ENCODERS);
      }

   } // reset_left_drive_encoder

   //--------------------------------------------------------------------------
   //
   // reset_right_drive_encoder
   //

   /**
    * Reset the right drive wheel encoder.
    */
   public void reset_right_drive_encoder()

   {
      if (DriveMotor2 != null)
      {
         DriveMotor2.setMode(DcMotorController.RunMode.RESET_ENCODERS);
      }

   } // reset_right_drive_encoder

   //--------------------------------------------------------------------------
   //
   // reset_drive_encoders
   //

   /**
    * Reset both drive wheel encoders.
    */
   public void reset_drive_encoders()

   {
      //
      // Reset the motor encoders on the drive wheels.
      //
      reset_left_drive_encoder();
      reset_right_drive_encoder();

   } // reset_drive_encoders

   //--------------------------------------------------------------------------
   //
   // a_left_encoder_count
   //

   /**
    * Access the left encoder's count.
    */
   int a_left_encoder_count()
   {
      int l_return = 0;

      if (DriveMotor1 != null)
      {
         l_return = DriveMotor1.getCurrentPosition();
      }

      return l_return;

   } // a_left_encoder_count

   //--------------------------------------------------------------------------
   //
   // a_right_encoder_count
   //

   /**
    * Access the right encoder's count.
    */
   int a_right_encoder_count()

   {
      int l_return = 0;

      if (DriveMotor2 != null)
      {
         l_return = DriveMotor2.getCurrentPosition();
      }

      return l_return;

   } // a_right_encoder_count

   //--------------------------------------------------------------------------
   //
   // has_left_drive_encoder_reached
   //

   /**
    * Indicate whether the left drive motor's encoder has reached a value.
    */
   boolean has_left_drive_encoder_reached(double p_count)

   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      if (DriveMotor1 != null)
      {
         //
         // Has the encoder reached the specified values?
         //
         // TODO Implement stall code using these variables.
         //
         if (Math.abs(DriveMotor1.getCurrentPosition()) > p_count)
         {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
         }
      }

      //
      // Return the status.
      //
      return l_return;

   } // has_left_drive_encoder_reached

   //--------------------------------------------------------------------------
   //
   // has_right_drive_encoder_reached
   //

   /**
    * Indicate whether the right drive motor's encoder has reached a value.
    */
   boolean has_right_drive_encoder_reached(double p_count)

   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      if (DriveMotor2 != null)
      {
         //
         // Have the encoders reached the specified values?
         //
         // TODO Implement stall code using these variables.
         //
         if (Math.abs(DriveMotor2.getCurrentPosition()) > p_count)
         {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
         }
      }

      //
      // Return the status.
      //
      return l_return;

   } // has_right_drive_encoder_reached

   //--------------------------------------------------------------------------
   //
   // have_drive_encoders_reached
   //

   /**
    * Indicate whether the drive motors' encoders have reached a value.
    */
   boolean have_drive_encoders_reached(double p_left_count, double p_right_count)
   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      //
      // Have the encoders reached the specified values?
      //
      if (has_left_drive_encoder_reached(p_left_count) &&
         has_right_drive_encoder_reached(p_right_count))
      {
         //
         // Set the status to a positive indication.
         //
         l_return = true;
      }

      //
      // Return the status.
      //
      return l_return;

   } // have_encoders_reached

   //--------------------------------------------------------------------------
   //
   // drive_using_encoders
   //

   /**
    * Indicate whether the drive motors' encoders have reached a value.
    */
   boolean drive_using_encoders
   (
      double p_left_power
      , double p_right_power
      , double p_left_count
      , double p_right_count
   )

   {
      //
      // Assume the encoders have not reached the limit.
      //
      boolean l_return = false;

      //
      // Tell the system that motor encoders will be used.
      //
      run_using_encoders();

      //
      // Start the drive wheel motors at full power.
      //
      set_drive_power(p_left_power, p_right_power);

      //
      // Have the motor shafts turned the required amount?
      //
      // If they haven't, then the op-mode remains in this state (i.e this
      // block will be executed the next time this method is called).
      //
      if (have_drive_encoders_reached(p_left_count, p_right_count))
      {
         //
         // Reset the encoders to ensure they are at a known good value.
         //
         reset_drive_encoders();

         //
         // Stop the motors.
         //
         set_drive_power(0.0f, 0.0f);

         //
         // Transition to the next state when this method is called
         // again.
         //
         l_return = true;
      }

      //
      // Return the status.
      //
      return l_return;

   } // drive_using_encoders

   //--------------------------------------------------------------------------
   //
   // has_left_drive_encoder_reset
   //

   /**
    * Indicate whether the left drive encoder has been completely reset.
    */
   boolean has_left_drive_encoder_reset()
   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      //
      // Has the left encoder reached zero?
      //
      if (a_left_encoder_count() == 0)
      {
         //
         // Set the status to a positive indication.
         //
         l_return = true;
      }

      //
      // Return the status.
      //
      return l_return;

   } // has_left_drive_encoder_reset

   //--------------------------------------------------------------------------
   //
   // has_right_drive_encoder_reset
   //

   /**
    * Indicate whether the left drive encoder has been completely reset.
    */
   boolean has_right_drive_encoder_reset()
   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      //
      // Has the right encoder reached zero?
      //
      if (a_right_encoder_count() == 0)
      {
         //
         // Set the status to a positive indication.
         //
         l_return = true;
      }

      //
      // Return the status.
      //
      return l_return;

   } // has_right_drive_encoder_reset

   //--------------------------------------------------------------------------
   //
   // have_drive_encoders_reset
   //

   /**
    * Indicate whether the encoders have been completely reset.
    */
   boolean have_drive_encoders_reset()
   {
      //
      // Assume failure.
      //
      boolean l_return = false;

      //
      // Have the encoders reached zero?
      //
      if (has_left_drive_encoder_reset() && has_right_drive_encoder_reset())
      {
         //
         // Set the status to a positive indication.
         //
         l_return = true;
      }

      //
      // Return the status.
      //
      return l_return;

   } // have_drive_encoders_reset
   //--------------------------------------------------------
   // Encoder telemetry
   //--------------------------------------------------------
    /* public void update_telemetry ()

    {
        if (a_warning_generated ())
        {
            set_first_message (a_warning_message ());
        }
        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
            ( "01"
            , "Left Drive: "
                + a_left_drive_power ()
                + ", "
                + a_left_encoder_count ()
            );
        telemetry.addData
            ( "02"
            , "Right Drive: "
                + a_right_drive_power ()
                + ", "
                + a_right_encoder_count ()
            );
        telemetry.addData
            ( "03"
            , "Left Arm: " + a_left_arm_power ()
            );
        telemetry.addData
            ( "04"
            , "Hand Position: " + a_hand_position ()
            );

    } // update_telemetry*/
}
