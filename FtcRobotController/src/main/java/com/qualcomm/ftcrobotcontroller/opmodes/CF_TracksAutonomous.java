package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author Ryley Hindman
 * @version 2015-08-01-06-01
 */
public class CF_TracksAutonomous extends CF_Hardware

{
   public enum AutoMtnStates_E
   {
      AutoMtnInit,
      DriveAwayFromWall,
      TurnTowardMtn,
      DriveTowardMtn,
      RaiseWinch,
      ExtendWinch,
      LowerWinchToBar,
      RetractWinchAndDrive,
      EndAuto
   }

   public AutoMtnStates_E AutoMtnState = AutoMtnStates_E.AutoMtnInit;

   //--------------------------------------------------------------------------
   //
   // PushBotAuto
   //

   /**
    * Construct the class.
    * <p/>
    * The system calls this member when the class is instatiated.
    */
   public CF_TracksAutonomous()

   {
      //
      // Initialize base classes.
      //
      // All via self-construction.

      //
      // Initialize class members.
      //
      // All via self-construction.

   } // PushBotAuto

   //--------------------------------------------------------------------------
   //
   // start
   //

   /**
    * Perform any actions that are necessary when the OpMode is enabled.
    * <p/>
    * The system calls this member once when the OpMode is enabled.
    */
   @Override
   public void start()

   {
      //
      // Call the PushBotHardware (super/base class) start method.
      //
      super.start();

      //
      // Reset the motor encoders on the drive wheels.
      //
      reset_drive_encoders();

   } // start

   //--------------------------------------------------------------------------
   //
   // loop
   //

   /**
    * Implement a state machine that controls the robot during auto-operation.
    * The state machine uses a class member and encoder input to transition
    * between states.
    * <p/>
    * The system calls this member repeatedly while the OpMode is running.
    */
   @Override
   public void loop()

   {
      // State machine for parking on mountain in autonomous mode
      switch (AutoMtnState)
      {
         case AutoMtnInit:
            // Reset the encoders to ensure they are at a known good value.
            reset_drive_encoders();

            AutoMtnState = AutoMtnStates_E.DriveTowardMtn;

            break;

         // Drive forward until the encoders exceed the specified values.
         case DriveTowardMtn:
            // Tell the system that motor encoders will be used.  This call MUST
            // be in this state and NOT the previous or the encoders will not
            // work.  It doesn't need to be in subsequent states.
            run_using_encoders();

            // Start the drive wheel motors at full power.
            set_drive_power(1.0f, 1.0f);

            // Have the motor shafts turned the required amount?
            //
            // If they haven't, then the op-mode remains in this state (i.e this
            // block will be executed the next time this method is called).
            //5000 revs = 31 in

            if (have_drive_encoders_reached(500, 500))
            {
               // Reset the encoders to ensure they are at a known good value.
               reset_drive_encoders();

               // Stop the motors.
               set_drive_power(0.0f, 0.0f);

               // After robot is away from wall, turn toward mountain.
               AutoMtnState = AutoMtnStates_E.TurnTowardMtn;
            }
            break;

         case TurnTowardMtn:
            run_using_encoders();

            // Start the drive wheel motors at full power.
            set_drive_power(0.50f, -0.50f);

            // Tank turn until pointing at mountain
            if (have_drive_encoders_reached(100, -100))
            {
               // Reset the encoders to ensure they are at a known good value.
               reset_drive_encoders();

               // Stop the motors.
               set_drive_power(0.0f, 0.0f);
            }

            // After robot is away from wall, turn toward mountain.
            AutoMtnState = AutoMtnStates_E.EndAuto;
            break;

         case EndAuto:
         default:
            //
            // The autonomous actions have been accomplished (i.e. the state has
            // transitioned into its final state.
            //
            break;
      }
      // Realign the motors
      // I'm not quite sure where to put this method, but I'll figure it out.
      //cf_realign_encoders();

      //
      // Send telemetry data to the driver station.
      //

      // Get the encoder count for both the left and right encoders
      int cfLeftCount = a_left_encoder_count();
      int cfRightCount = a_right_encoder_count();

      //update_telemetry (); // Update common telemetry
      telemetry.addData("18", "State: " + v_state);
      //This updates the encoder state for the left encoder
      telemetry.addData("19", "Left Count: " + cfLeftCount);
      //This updates the encoder state for the right encoder
      telemetry.addData("20", "Right Count: " + cfRightCount);

      telemetry.addData("21", "Left Drive Power: " + GetDriveMotorPower1());

      telemetry.addData("22", "Right Drive Power: " + GetDriveMotorPower2());
   } // loop

   //--------------------------------------------------------------------------
   //
   // v_state
   //
   /**
    * This class member remembers which state is currently active.  When the
    * start method is called, the state will be initialized (0).  When the loop
    * starts, the state will change from initialize to state_1.  When state_1
    * actions are complete, the state will change to state_2.  This implements
    * a state machine for the loop method.
    */
   private int v_state = 0;

} // PushBotAuto
