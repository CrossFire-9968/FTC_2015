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
public class CF_BeaconTest extends Jeffs_Hardware
{
   // States used in autonomous mountain parking
   public enum States
   {
      DriveFromWall,
      TurnParallelToWall,
      IntermediateStateClearDebris,
      ClearDebris,
      BackUp,
      TurnToBeacon,
      IntermediateStateGoForward,
      GoForward,
      TankTurn,
      DumpClimbers,
      EndAuto,
   }
   private int sumDrive1 = 0;
   private int sumDrive2 = 0;
   private States currentState;
   private States nextStateAfterWait;

   private boolean doDriveFromWallFirstTimeFlag = true;
   private boolean doTurnParallelToWallFirstTimeFlag = true;
   private boolean doClearDebrisFirstTimeFlag = true;
   private boolean doBackUpFirstTimeFlag = true;
   private boolean doTankTurnFirstTimeFlag = true;
   private boolean doIntermediateStateGoForwardFirstTimeFlag = true;
   private double HangerServoPosition = 0.0;
   final double HangerServoStopPosition = 0.5;


   //--------------------------------------------------------------------------
   // Method:  PushBotAuto
   // Desc:    Constructor
   //--------------------------------------------------------------------------
   public CF_BeaconTest()
   {
      // Initialize base classes.
      // All via self-construction.

      // Initialize class members.
      // All via self-construction.
   }

   private void runningSum(int leftCount, int rightCount) {
      sumDrive1 = sumDrive1 + leftCount;
      sumDrive2 = sumDrive2 + rightCount;
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
      HangerServoPosition = BucketServo.getPosition();
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

         case TurnParallelToWall :
            doTurnParallelToWall();
            break;

         case IntermediateStateClearDebris:
            doIntermediateStateClearDebris();
            break;

         case ClearDebris:
            doClearDebris();
            break;

         case BackUp:
            doBackUp();
            break;

         case TankTurn:
            doTankTurn();
            break;

         case IntermediateStateGoForward:
            doIntermediateStateGoForward();
            break;

         case GoForward:
            doGoForward();
            break;

         case DumpClimbers:
            doDumpClimbers();
            break;
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
      if (doDriveFromWallFirstTimeFlag)
      {
         runningSum(-11200, -11300);
         doDriveFromWallFirstTimeFlag = false;
      }

      final double drive1Power = 0.265f;
      final double drive2Power = 0.25f;
//      final int newMotorPosition1 = sumDrive1;
//      final int newMotorPosition2 = sumDrive2;
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
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(drive1Power, drive2Power);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         // Reset motor encoders
         //ResetDriveEncoders();

         // Tell state machine to wait for encoder reset before moving on the next motor command
         // and specify the state we want to enter after encoders reset
         //currentState = States.WaitForEncoderReset;
         //nextStateAfterWait = States.TurnTowardMtn;
         ResetDriveEncoders();
         currentState = States.EndAuto;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doBackUp
   // Desc:    Turn on motors to drive straight from wall out onto arena.
   //--------------------------------------------------------------------------
   private void doTurnParallelToWall()
   {
//      final int countsToMoveMotor1 = 1000;
//      final int countsToMoveMotor2 = 1000;
      if(doTurnParallelToWallFirstTimeFlag) {
         runningSum(-1000, 0);
         doTurnParallelToWallFirstTimeFlag = false;
         telemetry.addData("FLAG", "FLAG");
      }
      final double drive1Power = 0.25f;
      final double drive2Power = 0.25f;
//      final int newMotorPosition1 = -11600;
//      final int newMotorPosition2 = -9700;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Get current encoder motor counts
//      newMotorPosition1 = GetDrive1MotorCounts() + countsToMoveMotor1;
//      newMotorPosition2 = GetDrive2MotorCounts() + countsToMoveMotor2;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: TurnParallelToWall");

      // Set the drive motor encoders to use RUN_TO_POSITION
      SetRunToPositionMode();

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(drive1Power, drive2Power);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached))
      {
         // Reset motor encoders
         //ResetDriveEncoders();

         // Tell state machine to wait for encoder reset before moving on the next motor command
         // and specify the state we want to enter after encoders reset
         //currentState = States.WaitForEncoderReset;
         //nextStateAfterWait = States.TurnTowardMtn;
         currentState = States.IntermediateStateClearDebris;
      }
   }

   private void doIntermediateStateClearDebris() {
      if(doClearDebrisFirstTimeFlag) {
         runningSum(-2500, -2500);
         doClearDebrisFirstTimeFlag = false;
         telemetry.addData("FLAG AGAIN", "FLAG AGAIN");
      }
      currentState = States.ClearDebris;
   }

   //--------------------------------------------------------------------------
   // Method:  doTurnTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doClearDebris()
   {

//      final int countsToMoveMotor1 = -8000;
//      final int countsToMoveMotor2 = -6000;
      final double leftDrivePower = 0.25f;
      final double rightDrivePower = 0.25f;
//      int newMotorPosition1 = -1100;
//      int newMotorPosition2 = -9000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: ClearDebris");

      // Get current encoder motor counts
//      newMotorPosition1 = GetDrive1MotorCounts() + countsToMoveMotor1;
//      newMotorPosition2 = GetDrive2MotorCounts() + countsToMoveMotor2;

      // Set the drive motor encoders to use RUN_TO_POSITION
      //SetRunToPositionMode();

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached) || (drive2PositionReached))
      {
         //Reset motor encoders
         //ResetDriveEncoders();

         //Tell state machine to wait for encoder reset before moving on the next motor command
         //and specify the state we want to enter after encoders reset

         currentState = States.BackUp;
//         nextStateAfterWait = States.EndAuto;
      }
   }


   //--------------------------------------------------------------------------
   // Method:  doDriveTowardMtn
   // Desc:
   //--------------------------------------------------------------------------
   private void doBackUp()
   {
//      final int countsToMoveMotor1 = -1100
//      final int countsToMoveMotor2 = -9000
      if(doBackUpFirstTimeFlag){
         runningSum(1000, 1000);
         doBackUpFirstTimeFlag = false;
      }
      final double leftDrivePower = 0.25f;
      final double rightDrivePower = 0.25f;
//      int newMotorPosition1 = -9000;
//      int newMotorPosition2 = -7000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: BackUp");

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if ((drive1PositionReached))
      {
         //Reset motor encoders
         //ResetDriveEncoders();

         //Tell state machine to wait for encoder reset before moving on the next motor command
         //and specify the state we want to enter after encoders reset

         currentState = States.TankTurn;
//         nextStateAfterWait = States.EndAuto;
      }
   }



   //--------------------------------------------------------------------------
   // Method:  doTankTurn
   //--------------------------------------------------------------------------
   // Method:  doLowerWinchToBar
   // Desc:
   // Desc:
   //--------------------------------------------------------------------------
   private void doTankTurn()
   {
      if(doTankTurnFirstTimeFlag){
         runningSum(1100, -1100);
         doTankTurnFirstTimeFlag = false;
      }
//      final int countsToMoveMotor1 = -1100
//      final int countsToMoveMotor2 = -9000
      final double leftDrivePower = 0.15f;
      final double rightDrivePower = 0.15f;
//      int newMotorPosition1 = -7000;
//      int newMotorPosition2 = -9000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: TankTurn");

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if (drive2PositionReached)
      {
         //Reset motor encoders
         //ResetDriveEncoders();

         //Tell state machine to wait for encoder reset before moving on the next motor command
         //and specify the state we want to enter after encoders reset

         currentState = States.IntermediateStateGoForward;
//         nextStateAfterWait = States.EndAuto;
      }
   }
   private void doIntermediateStateGoForward() {
      if(doIntermediateStateGoForwardFirstTimeFlag) {
         runningSum(-300, -300);
         doIntermediateStateGoForwardFirstTimeFlag = false;
         telemetry.addData("FLAG AGAIN", "FLAG AGAIN");
      }
      currentState = States.GoForward;
   }

   private void doGoForward()
   {

//      final int countsToMoveMotor1 = -1100
//      final int countsToMoveMotor2 = -9000
      final double leftDrivePower = 0.15f;
      final double rightDrivePower = 0.15f;
//      int newMotorPosition1 = -7000;
//      int newMotorPosition2 = -9000;
      boolean drive1PositionReached = false;
      boolean drive2PositionReached = false;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: GoForward");

      // set desired encoder counts to move
      SetDesiredDriveEncoderPositions(sumDrive1, sumDrive2);

      // Set motor power to drive straight forward until position reached
      SetDriveMotorPower(leftDrivePower, rightDrivePower);

      // Check to see if encoders have reached desired position
      drive1PositionReached = Drive1EncodersReachedPosition(sumDrive1);
      drive2PositionReached = Drive2EncodersReachedPosition(sumDrive2);

      // If either encoder has reached the desired position, turn off motors and reset encoders
      if (drive2PositionReached)
      {
         //Reset motor encoders
         //ResetDriveEncoders();

         //Tell state machine to wait for encoder reset before moving on the next motor command
         //and specify the state we want to enter after encoders reset

         currentState = States.DumpClimbers;
//         nextStateAfterWait = States.EndAuto;
      }
   }

   //--------------------------------------------------------------------------
   private void doDumpClimbers()
   {
      HangerServoPosition = GetBucketServoPosition();

      while(HangerServoPosition >= HangerServoStopPosition){
         SetBucketServoPosition(HangerServoPosition - 0.001);
      }
//      if (HangerServoPosition >= HangerServoStopPosition)
//      {
//         SetBucketServoPosition(HangerServoStopPosition);
//      }

      currentState = States.EndAuto;

      // Show state on driver app for debug purposes
      telemetry.addData("1", "State: DumpClimbers");
      telemetry.addData("4", BucketServo.getPosition());

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
      final double leftDrivePower = 0.0f;
      final double rightDrivePower = 0.0f;
      RunWithoutDriveEncoders();
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

