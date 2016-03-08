package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// CF_AutoMtnPark
//

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author Lauren Dawson
 */
public class CF_AutoMtnPark extends CF_Hardware
{
   final static double OFF = 0.0f;

   // States used in autononomous mountain parking
   public enum States
   {
      DriveFromWall,
      TurnTowardMtn,
      DriveToMtn,
      RaiseWinch,
      ExtendWinch,
      LowerWinchToBar,
      RetractWinchAndDrive,
      EndAuto
   }

   private States currentState;

   //--------------------------------------------------------------------------
   // Method:  PushBotAuto
   // Desc:    Constructor
   //--------------------------------------------------------------------------
   public CF_AutoMtnPark()
   {
      // Initialize base classes.
      // All via self-construction.

      // Initialize class members.
      // All via self-construction.
   }

   //--------------------------------------------------------------------------
   // Method:  Start
   // Desc:    Perform any actions that are necessary when the OpMode is enabled.
   //          The system calls this member once when the OpMode is enabled.
   //--------------------------------------------------------------------------
   @Override
   public void start()
   {
      // Call the PushBotHardware (super/base class) start method.
      super.start();

      // Set the drive motor encoders to use RUN_TO_POSITION
      SetRunToPositionMode();

      // Reset the motor encoders on the drive wheels.  Reset encoders sets
      // the count values to zero.  It takes one iteration to complete the action
      // Reset here so encoders are ready by the time we enter first state
      reset_drive_encoders();

      // Set state machine to the state we want to start in
      currentState = States.DriveFromWall;
   }


   //--------------------------------------------------------------------------
   // Method:  Loop
   // Desc:    Implement a state machine that controls the robot during
   //          auto-operation.  The state machine uses a class member and encoder
   //          input to transition between states. The system calls this member
   //          repeatedly while the OpMode is running.
   //--------------------------------------------------------------------------
   @Override
   public void loop()
   {
      // Autonomous mountain park state machine
      switch (currentState)
      {
         case DriveFromWall:
            doDriveFromWall();
            break;

         case TurnTowardMtn:
            doTurnTowardMtn();
            break;

         case DriveToMtn:
            doDriveToMtn();
            break;

         case RaiseWinch:
            doRaiseWinch();
            break;

         case ExtendWinch:
            doExtendWinch();
            break;

         case LowerWinchToBar:
            doLowerWinchToBar();
            break;

         case RetractWinchAndDrive:
            doRetractWinchAndDrive();
            break;

         case EndAuto:
            doEndAuto();
            break;

         default:
            break;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doDriveAwayFromWall
   // Desc:    Turn on motors to drive straight from wall out onto arena.
   //--------------------------------------------------------------------------
//   private void doDriveAwayFromWall()
//   {
//      final double leftDrivePower = 0.5f;
//      final double rightDrivePower = 0.5f;
//      final double leftCounts = 2880;
//      final double rightCounts = 2880;
//      boolean finalPositionReached = false;
//      boolean waitForEncoderReset = false;
//      boolean encoderResetComplete = false;
//
//      // Show state on driver app for debug purposes
//      telemetry.addData("1", "State: DriveAwayFromWall");
//
//      // Tell the system that motor encoders will be used
//      run_using_encoders();
//
//      // Set motor power to drive straight forward
//      set_drive_power(leftDrivePower, rightDrivePower);
//
//      // Have the motor shafts turned the required amount?
//      finalPositionReached = have_drive_encoders_reached(leftCounts, rightCounts);
//
//      // When final position reached, reset encoders and stop moving
//      if (finalPositionReached)
//      {
//         // Reset the encoders to ensure they are at a known good value
//         // by the time enter into the next state.  Takes one task to complete
//         reset_drive_encoders();
//
//         // Resetting encoders takes one complete task to complete.  Setting this flag
//         // causes software to wait until encoders reset before advancing to next state.
//         waitForEncoderReset = true;
//
//         // Stop the motors
//         set_drive_power(OFF, OFF);
//      }
//
//      // Wait for encoders to reset, if they don't, we will just hand out in this state
//      if (waitForEncoderReset)
//      {
//         // Check if encoders have reset
//         encoderResetComplete = have_drive_encoders_reset();
//
//         // After encoders reset, time to turn and face mountain
//         if(encoderResetComplete)
//         {
//            currentState = States.TurnTowardMtn;
//         }
//      }
//   }


   //--------------------------------------------------------------------------
   // Method:  doDriveAwayFromWall
   // Desc:    Turn on motors to drive straight from wall out onto arena.
   //--------------------------------------------------------------------------
   private void doDriveFromWall()
   {
      final int countsToMoveMotor1 = 1000;
      final int countsToMoveMotor2 = 1000;
      final double leftDrivePower = 0.5f;
      final double rightDrivePower = 0.5f;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: DriveFromWall");

      // Start out driving in dozer configuration
      SetDriveConfig(DriveConfig_E.DOZER);

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(countsToMoveMotor1, countsToMoveMotor2);

      // Set motor power to drive straight forward until position reached
      set_drive_power(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(countsToMoveMotor1);
      drive2PositionReached = Drive2EncodersReachedPosition(countsToMoveMotor2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         set_drive_power(OFF, OFF);
         reset_drive_encoders();

         // Time to turn toward mountain
         currentState = States.TurnTowardMtn;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doTurnTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doTurnTowardMtn()
   {
      final int countsToMoveMotor1 = 500;
      final int countsToMoveMotor2 = -500;
      final double leftDrivePower = 0.25f;
      final double rightDrivePower = 0.25f;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: TurnTowardMtn");

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(countsToMoveMotor1, countsToMoveMotor2);

      // Set motor power to drive straight forward until position reached
      set_drive_power(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(countsToMoveMotor1);
      drive2PositionReached = Drive2EncodersReachedPosition(countsToMoveMotor2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         set_drive_power(OFF, OFF);
         reset_drive_encoders();

         // Time to turn toward mountain
         currentState = States.DriveToMtn;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doDriveTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doDriveToMtn()
   {
      final int countsToMoveMotor1 = 500;
      final int countsToMoveMotor2 = 500;
      final double leftDrivePower = 0.5f;
      final double rightDrivePower = 0.5f;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: DriveToMtn");

      // Switch to driving in mountain configuration
      SetDriveConfig(DriveConfig_E.MOUNTAIN);

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(countsToMoveMotor1, countsToMoveMotor2);

      // Set motor power to drive straight forward until position reached
      set_drive_power(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(countsToMoveMotor1);
      drive2PositionReached = Drive2EncodersReachedPosition(countsToMoveMotor2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         set_drive_power(OFF, OFF);
         reset_drive_encoders();

         // Time to turn toward mountain
         currentState = States.RaiseWinch;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doRaiseWinch
   // Desc:
   //--------------------------------------------------------------------------
   private void doRaiseWinch()
   {
      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: RaiseWinch");
      currentState = States.ExtendWinch;
   }


   //--------------------------------------------------------------------------
   // Method:  doExtendWinch
   // Desc:
   //--------------------------------------------------------------------------
   private void doExtendWinch()
   {
      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: ExtendWinch");
      currentState = States.LowerWinchToBar;
   }


   //--------------------------------------------------------------------------
   // Method:  doLowerWinchToBar
   // Desc:
   //--------------------------------------------------------------------------
   private void doLowerWinchToBar()
   {
      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: LowerWinchToBar");
      currentState = States.RetractWinchAndDrive;
   }


   //--------------------------------------------------------------------------
   // Method:  doRetractWinchAndDrive
   // Desc:
   //--------------------------------------------------------------------------
   private void doRetractWinchAndDrive()
   {
      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: RetractWinchAndDrive");
      currentState = States.EndAuto;
   }


   //--------------------------------------------------------------------------
   // Method:  doEndAuto
   // Desc:
   //--------------------------------------------------------------------------
   private void doEndAuto()
   {
      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: EndAuto");
   }
}

