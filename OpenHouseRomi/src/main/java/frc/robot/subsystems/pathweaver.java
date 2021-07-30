package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Filesystem;
import java.io.IOException;
import java.nio.file.Path;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class pathweaver extends SubsystemBase
{
    private final String PARENT_FOLDER = "paths/output/";// "paths/output/path2.wpilib.json"
    private final String FILE_TYPE = ".wpilib.json"; 
    private String path_name = "";
    private String path_dir = "";
    private Trajectory trajectory = null; 

    public void begin(String path_name)
    {
        this.path_name = path_name;
        path_dir = PARENT_FOLDER + path_name + FILE_TYPE; 
        Path trajectory_path = Filesystem.getDeployDirectory().toPath().resolve(path_dir);
        
        try{ trajectory = TrajectoryUtil.fromPathweaverJson(trajectory_path);
        System.out.println("pathweaver.java: pathweaver trajectory found!       Directory: " + trajectory_path + "\n");} 
        catch (IOException e){System.out.println("Wrong directory!");}      
    }

    public Trajectory getTrajectory(){System.out.println("returning trajectory! \n"); return trajectory; 

}
    public String getPathName(){return path_name; }
}
