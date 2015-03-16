
import edu.rit.image.ByteImageQueue;
import edu.rit.image.Color;
import edu.rit.util.AList;
import edu.rit.util.Random;

/**
 * Class InfectionState provides the state for a network infection process.
 *
 * @author  Alan Kaminsky
 * @version 28-Feb-2015
 */
public class InfectionState
   {
   // Vertex state values.
   public static final byte SUSCEPTIBLE = (byte)0;
   public static final byte INFECTED    = (byte)1;
   public static final byte RECOVERED   = (byte)2;
   public static final byte DEAD        = (byte)3;

   // Vertex state color palette.
   private static final AList<Color> palette = new AList<Color>();
   static
      {
      palette.addLast (new Color() .rgb (255, 255, 0)); // yellow
      palette.addLast (new Color() .rgb (255,   0, 0)); // red
      palette.addLast (new Color() .rgb (  0, 255, 0)); // green
      palette.addLast (new Color() .rgb (  0,   0, 0)); // black
      }

   // Graph.
   private Graph g;
   private int V;

   // Model parameters.
   private double infprob;
   private int inftime;
   private int rectime;
   private double dieprob;

   // Pseudorandom number generator.
   private Random prng;

   // State of each vertex.
   private byte[] state;
   private byte[] nextState;

   // Infection time remaining for each vertex.
   private int[] infTimeRemaining;

   // Recovered time remaining for each vertex.
   private int[] recTimeRemaining;

   // Number of steps taken.
   private int step;

   // Construct a new infection state object for the given graph. Initially all
   // vertices are susceptible.
   public InfectionState
      (Graph g,
       double infprob,
       int inftime,
       int rectime,
       double dieprob,
       Random prng)
      {
      this.g = g;
      this.V = g.vcount();
      this.infprob = infprob;
      this.inftime = inftime;
      this.rectime = rectime;
      this.dieprob = dieprob;
      this.prng = prng;
      state = new byte [V];
      nextState = new byte [V];
      infTimeRemaining = new int [V];
      recTimeRemaining = new int [V];
      }

   // Clear this infection state object. All vertices are susceptible.
   public void clear()
      {
      for (int i = 0; i < V; ++ i)
         state[i] = SUSCEPTIBLE;
      step = 0;
      }

   // Returns the state of the given vertex.
   public byte getState
      (int i)
      {
      return state[i];
      }

   // Set the state of the given vertex to the given value.
   public void setState
      (int i,
       byte value)
      {
      if (SUSCEPTIBLE > value || value > DEAD)
         throw new IllegalArgumentException (String.format
            ("InfectionState.setState(): value = % illegal", value));
      state[i] = value;
      if (value == INFECTED)
         infTimeRemaining[i] = inftime;
      }

   // Returns the infection rate = fraction of infected vertices.
   public double infectionRate()
      {
      int count = 0;
      for (int i = 0; i < V; ++ i)
         if (state[i] == INFECTED)
            ++ count;
      return (double)count/V;
      }

   // Step to the next state. Returns the infection rate = fraction of infected
   // vertices in the next state.
   public double step()
      {
      int count = 0;
      for (int i = 0; i < V; ++ i)
         {
         double rand = prng.nextDouble();
         if (state[i] == SUSCEPTIBLE)
            {
            // Determine if any adjacent vertex is infected.
            boolean adjinf = false;
            Graph.Vertex v_i = g.v(i);
            int D = v_i.degree();
            for (int j = 0; ! adjinf && j < D; ++ j)
               adjinf = state[v_i.adj(j).vnum] == INFECTED;

            // If so, infect this vertex with probability infprob.
            if (adjinf && rand < infprob)
               {
               nextState[i] = INFECTED;
               infTimeRemaining[i] = inftime;
               ++ count;
               }
            else
               nextState[i] = SUSCEPTIBLE;
            }
         else if (state[i] == INFECTED)
            {
            // If infection has run its course, recover or die.
            -- infTimeRemaining[i];
            if (infTimeRemaining[i] > 0)
               {
               nextState[i] = INFECTED;
               ++ count;
               }
            else if (rand < dieprob)
               nextState[i] = DEAD;
            else
               {
               nextState[i] = RECOVERED;
               recTimeRemaining[i] = rectime;
               }
            }
         else if (state[i] == RECOVERED)
            {
            // If recovery has run its course, become susceptible again.
            -- recTimeRemaining[i];
            if (recTimeRemaining[i] > 0)
               nextState[i] = RECOVERED;
            else
               nextState[i] = SUSCEPTIBLE;
            }
         else
            nextState[i] = state[i];
         }
      byte[] tmp = state; state = nextState; nextState = tmp;
      ++ step;
      return (double)count/V;
      }

   // Returns the number of steps taken.
   public int steps()
      {
      return step;
      }

   // Put the current state into the row of the given image queue corresponding
   // to the current step.
   public void putStateImage
      (ByteImageQueue queue)
      throws InterruptedException
      {
      queue.put (step, state);
      }

   // Returns the image palette.
   public static AList<Color> palette()
      {
      return palette;
      }
   }