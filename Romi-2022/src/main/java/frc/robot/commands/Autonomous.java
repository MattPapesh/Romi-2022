package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.PathweaverProject;

import java.util.LinkedList;

// Purpose: Create autonomous routines that can be ran during the autonomous period. 

// NOTE: Only 1 routine can run at a time; this is done by getting a routine name that's passed in via the constructor
// and so if there is a routine with the name provided it will be added the the autonomous command. 
// THEREFORE ALL ROUTINES SHOULD HAVE DIFFERENT NAMES!

// NOTE: A "routine" is just a combination of commands that chained together to be scheduled and ran consecutively. 

public class Autonomous extends SequentialCommandGroup
{
    private class routine extends SequentialCommandGroup{
        String routine_name = "";

        public routine(String routine_name){
            this.routine_name = routine_name; 
        }
    };

    private LinkedList<routine> routine_list = new LinkedList<routine>(); 

    public Autonomous(Drivetrain drivetrain, String routine_name){
        AutonomousDrive my_auto_drive = new AutonomousDrive(drivetrain, new PathweaverProject(drivetrain, "foward_and_back")); 

        // Create routines by passing in a name and a sequential command group!
        createRoutine("myRoutine", my_auto_drive.getPathGroupCommand("foward").
        andThen(new InstantCommand(() -> {System.out.println("test!");})));//.
        //andThen(my_auto_drive.getPathGroupCommand("back")));

        // Once all routines are created, the requested routine will be added to the autonomous command
        addCommands(getRoutineCommand(routine_name));
    }

    private void createRoutine(String routine_name, SequentialCommandGroup routine_command){
        routine_list.addLast(new routine(routine_name));
        routine_list.getLast().addCommands(routine_command);
    }

    private SequentialCommandGroup getRoutineCommand(String routine_name){
        for(int i = 0; i < routine_list.size(); i++){
            if(routine_list.get(i).routine_name == routine_name){
                return routine_list.get(i); 
            }
        }

        System.err.println("Autonomous.java: Warning! The requested autonomous routine wasn't found! \n");
        return null;
    }
}

