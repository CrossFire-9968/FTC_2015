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
public class CF_AutoMtnPark extends Jeffs_Hardware
{
   // States used in autonomous mountain parking
   public enum States
   {
      DriveFromWall,
      BackUp,
      TurnTowardMtn,
      DriveToMtn,
      RaiseWinch,
      ExtendWinch,
      LowerWinchToBar,
      RetractWinchAndDrive,
      EndAuto,
   }

   private States currentState;
   private States nextStateAfterWait;


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
   // NAME: Initialization method
   // DESC: Performs any actions that are necessary when the OpMode is enabled.
   //       The system calls this member once when the OpMode is enabled.
   //--------------------------------------------------------------------------
//   @Override
//   public void init()
//   {
//      // Reset the motor encoders on the drive wheels.  Reset encoders sets
//      // the count values to zero.  It takes one iteration to complete the action
//      // Reset here so encoders are ready by the time we enter first state
//      ResetDriveEncoders();
//   }

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

      // Reset the motor encoders on the drive wheels.  Reset encoders sets
      // the count values to zero.  It takes one iteration to complete the action
      // Reset here so encoders are ready by the time we enter first state
      ResetDriveEncoders();

      // Set state machine to the state we want to start in
      currentState = States.DriveFromWall;
      nextStateAfterWait = States.EndAuto;
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

         case BackUp:
            doBackUp();
            break;

         case TurnTowardMtn:
            doTurnTowardMtn();
            break;

         case DriveToMtn:
            doDriveToMtn();
            break;
//
//         case RaiseWinch:
//            doRaiseWinch();
//            break;
//
//         case ExtendWinch:
//            doExtendWinch();
//            break;
//
//         case LowerWinchToBar:
//            doLowerWinchToBar();
//            break;
//
//         case RetractWinchAndDrive:
//            doRetractWinchAndDrive();
//            break;
//
//         case EndAuto:
//            doEndAuto();
//            break;

         default:
            break;
      }

      telemetry.addData("2", "Drive1Counts: " + GetDrive1MotorCounts());
      telemetry.addData("3", "Drive2Counts: " + GetDrive2MotorCounts());
   }


   //--------------------------------------------------------------------------
   // Method:  doDriveAwayFromWall
   // Desc:    Turn on motors to drive straight from wall out onto arena.
   //--------------------------------------------------------------------------
   private void doDriveFromWall()
   {
//      final int countsToMoveMotor1 = 1000;
//      final int countsToMoveMotor2 = 1000;
      final double drive1Power = 0.2f;
      final double drive2Power = 0.2f;
      final int newMotorPosition1 = -8800;
      final int newMotorPosition2 = -8800;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Get current encoder motor counts
//      newMotorPosition1 = GetDrive1MotorCounts() + countsToMoveMotor1;
//      newMotorPosition2 = GetDrive2MotorCounts() + countsToMoveMotor2;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: DriveFromWall");

      // Set the drive motor encoders to use RUN_TO_POSITION
      SetRunToPositionMode();

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(newMotorPosition1, newMotorPosition2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(drive1Power, drive2Power);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(newMotorPosition1);
      drive2PositionReached = Drive2EncodersReachedPosition(newMotorPosition2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         // Reset motor encoders
         //ResetDriveEncoders();

         // Tell state machine to wait for encoder reset before moving on the next motor command
         // and specify the state we want to enter after encoders reset
         //currentState = States.WaitForEncoderReset;
         //nextStateAfterWait = States.TurnTowardMtn;
         currentState = States.BackUp;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doBackUp
   // Desc:    Turn on motors to drive straight from wall out onto arena.
   //--------------------------------------------------------------------------
   private void doBackUp()
   {
//      final int countsToMoveMotor1 = 1000;
//      final int countsToMoveMotor2 = 1000;
      final double drive1Power = 0.2f;
      final double drive2Power = 0.2f;
      final int newMotorPosition1 = -8300;
      final int newMotorPosition2 = -8300;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Get current encoder motor counts
//      newMotorPosition1 = GetDrive1MotorCounts() + countsToMoveMotor1;
//      newMotorPosition2 = GetDrive2MotorCounts() + countsToMoveMotor2;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: BackUp");

      // Set the drive motor encoders to use RUN_TO_POSITION
      SetRunToPositionMode();

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(newMotorPosition1, newMotorPosition2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(drive1Power, drive2Power);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(newMotorPosition1);
      drive2PositionReached = Drive2EncodersReachedPosition(newMotorPosition2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         // Reset motor encoders
         //ResetDriveEncoders();

         // Tell state machine to wait for encoder reset before moving on the next motor command
         // and specify the state we want to enter after encoders reset
         //currentState = States.WaitForEncoderReset;
         //nextStateAfterWait = States.TurnTowardMtn;
         currentState = States.TurnTowardMtn;
      }
   }

   //--------------------------------------------------------------------------
   // Method:  doTurnTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doTurnTowardMtn()
   {
//      final int countsToMoveMotor1 = 500;
//      final int countsToMoveMotor2 = -500;
      final double leftDrivePower = 0.20f;
      final double rightDrivePower = 0.20f;
      int newMotorPosition1 = -9600;
      int newMotorPosition2 = -7000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: TurnTowardMtn");

      // Get current encoder motor counts
//      newMotorPosition1 = GetDrive1MotorCounts() + countsToMoveMotor1;
//      newMotorPosition2 = GetDrive2MotorCounts() + countsToMoveMotor2;

      // Set the drive motor encoders to use RUN_TO_POSITION
      //SetRunToPositionMode();

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(newMotorPosition1, newMotorPosition2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(newMotorPosition1);
      drive2PositionReached = Drive2EncodersReachedPosition(newMotorPosition2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
          //Reset motor encoders
         //ResetDriveEncoders();

          //Tell state machine to wait for encoder reset before moving on the next motor command
          //and specify the state we want to enter after encoders reset

         currentState = States.DriveToMtn;
//         nextStateAfterWait = States.EndAuto;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doDriveTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doDriveToMtn()
   {
//      final int countsToMoveMotor1 = -9700;
//      final int countsToMoveMotor2 = -6900;
      final double leftDrivePower = 0.15f;
      final double rightDrivePower = 0.15f;
      int newMotorPosition1 = -600;
      int newMotorPosition2 = 2000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: DriveToMtn");

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(newMotorPosition1, newMotorPosition2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(newMotorPosition1);
      drive2PositionReached = Drive2EncodersReachedPosition(newMotorPosition2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         doEndAuto();
         // Reset motor encoders
//         ResetDriveEncoders();

         // Tell state machine to wait for encoder reset before moving on the next motor command
         // and specify the state we want to enter after encoders reset
//         currentState = States.WaitForEncoderReset;
//         nextStateAfterWait = States.EndAuto;
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


   //--------------------------------------------------------------------------
   // Name: doWaitForEncoderReset
   // Desc: Must wait for encoders to reset before we move on to next state.
   //       If we don't, then the encoder values may not be set back to zero.
   //       This could mess up our logic causing the states to act unpredictable
   //--------------------------------------------------------------------------
   private void doWaitForEncoderReset()
   {
      boolean resetComplete = false;

      // Have the encoders both reset?
      resetComplete = HaveDriveMotorEncodersReset();

      // Once encoders are reset, it's ok to move on to next state
      // otherwise hang out here.
      if (resetComplete)
      {
         // To make this state reusable, we are using a global variable to temporarily
         // hold the value of the next state we want to enter after the encoders reset.
         // This way, we don't have to have a bunch of wait state all doing the same
         // thing cluttering up the state machine each time we reset the motor encoders.
         // Now that the reset is complete, update the current state to the
         // next state we want to enter.
         currentState = nextStateAfterWait;
      }
   }
}

