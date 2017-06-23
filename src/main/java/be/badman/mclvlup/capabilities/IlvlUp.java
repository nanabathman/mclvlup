package be.badman.mclvlup.capabilities;

/**
 * Created by Kevin on 21-6-2017.
 */
public interface IlvlUp {
   void add (float points);
   void remove (float points);
   float getLvl();
   float getPoints();
    void sayLvl();
    void sayPoints();
    String getName();
    String getKey();
}
