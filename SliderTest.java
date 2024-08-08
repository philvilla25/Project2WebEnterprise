/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testJUnit;

import cst8218.stan0304.slider.entity.Slider;
import cst8218.stan0304.slider.resources.sliderNumbers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 *@aurthor shale
 */
class SliderTest {

    private Slider slider;

    @BeforeEach
    void setUp() {
        // Initialize a Slider object before each test
        slider = new Slider();
        slider.setX(2);
        slider.setY(3);
        slider.setSize(20);
        slider.setCurrentTravel(0);
        slider.setMaxTravel(10);
        slider.setMovementDirection(1);
        slider.setDirChangeCount(0);
    }

    /**
     * Test to check that the initial values are set correctly.
     */
    @Test
    void testInitialValues() {
        assertEquals(2, slider.getx(), "X position should be 2");
        assertEquals(3, slider.gety(), "Y position should be 3");
        assertEquals(20, slider.getSize(), "Size should be 20");
        assertEquals(0, slider.getCurrentTravel(), "Current travel should be 0");
        assertEquals(10, slider.getMaxTravel(), "Max travel should be 10");
        assertEquals(1, slider.getMovementDirection(), "Movement direction should be 1");
        assertEquals(0, slider.getDirChangeCount(), "Direction change count should be 0");
    }

    /**
     * Test the behavior of the Slider when no direction change occurs.
     * The currentTravel should increase by TRAVEL_SPEED and dirChangeCount should increment.
     */
    @Test
    void testTimeStep_NoDirectionChange() {
        slider.setCurrentTravel(0);
        slider.setMaxTravel(10);
        slider.timeStep();
        assertEquals(20, slider.getCurrentTravel(), "Current travel should increase by TRAVEL_SPEED (20)");
        assertEquals(1, slider.getDirChangeCount(), "Direction change count should be incremented by 1");
    }

    /**
     * Test the behavior of the Slider when a direction change is triggered.
     * The currentTravel should reverse direction, but dirChangeCount should remain the same.
     */
    @Test
    void testTimeStep_WithDirectionChange() {
        slider.setCurrentTravel(10);
        slider.setMaxTravel(10);
        slider.setMovementDirection(-1);
        slider.timeStep();
        assertEquals(-10, slider.getCurrentTravel(), "Current travel should reverse direction");
        assertEquals(1, slider.getDirChangeCount(), "Direction change count should remain the same");
    }

    /**
     * Test the behavior when the Slider reaches the maximum number of direction changes.
     * Max travel should decrease by DECAY_RATE and direction change count should reset.
     */
    @Test
    void testTimeStep_WithMaxDirChanges() {
        slider.setCurrentTravel(-10); // Move slider to -maxTravel
        slider.setDirChangeCount(sliderNumbers.MAX_DIR_CHANGES + 1); // Set to max changes
        slider.timeStep();
        assertEquals(9, slider.getMaxTravel(), "Max travel should decrease by DECAY_RATE (1)");
        assertEquals(0, slider.getDirChangeCount(), "Direction change count should reset to 0");
    }

    /**
     * Test the update method with valid values.
     * The slider’s attributes should be updated according to the newSlider values.
     */
    @Test
    void testUpdate() {
        Slider newSlider = new Slider();
        newSlider.setSize(30);
        newSlider.setX(5);
        newSlider.setY(6);
        newSlider.setCurrentTravel(15);
        newSlider.setMaxTravel(20);
        newSlider.setMovementDirection(-1);
        newSlider.setDirChangeCount(2);

        slider.update(newSlider);

        assertEquals(30, slider.getSize(), "Size should be updated to 30");
        assertEquals(5, slider.getx(), "X position should be updated to 5");
        assertEquals(6, slider.gety(), "Y position should be updated to 6");
        assertEquals(15, slider.getCurrentTravel(), "Current travel should be updated to 15");
        assertEquals(20, slider.getMaxTravel(), "Max travel should be updated to 20");
        assertEquals(-1, slider.getMovementDirection(), "Movement direction should be updated to -1");
        assertEquals(2, slider.getDirChangeCount(), "Direction change count should be updated to 2");
    }

    /**
     * Test the behavior when MaxTravel is set to 0.
     * The slider should not move, and direction change count should not change.
     */
    @Test
    void testTimeStep_HandlesEdgeCases() {
        slider.setCurrentTravel(0);
        slider.setMaxTravel(0); // MaxTravel is 0, should not move
        slider.timeStep();
        assertEquals(0, slider.getCurrentTravel(), "Current travel should remain 0");
        assertEquals(0, slider.getDirChangeCount(), "Direction change count should not change");
    }

    /**
     * Test the update method with invalid values.
     * Ensure that invalid values are not applied, and original valid values are preserved.
     */
    @Test
    void testUpdate_InvalidSliderValues() {
        Slider newSlider = new Slider();
        newSlider.setSize(0); // Invalid size
        newSlider.setX(0); // Invalid X position
        newSlider.setY(0); // Invalid Y position
        newSlider.setCurrentTravel(0); // Invalid current travel
        newSlider.setMaxTravel(0); // Invalid max travel
        newSlider.setMovementDirection(0); // Invalid direction
        newSlider.setDirChangeCount(0); // Invalid direction change count

        slider.update(newSlider);

        // Check that invalid values were not applied
        assertEquals(20, slider.getSize(), "Size should remain unchanged at 20");
        assertEquals(2, slider.getx(), "X position should remain unchanged at 2");
        assertEquals(3, slider.gety(), "Y position should remain unchanged at 3");
        assertEquals(0, slider.getCurrentTravel(), "Current travel should remain unchanged at 0");
        assertEquals(10, slider.getMaxTravel(), "Max travel should remain unchanged at 10");
        assertEquals(1, slider.getMovementDirection(), "Movement direction should remain unchanged at 1");
        assertEquals(0, slider.getDirChangeCount(), "Direction change count should remain unchanged at 0");
    }

    /**
     * Test the update method with a mix of valid and invalid values.
     * Ensure that valid values are applied and invalid ones are not.
     */
    @Test
    void testUpdate_ValidAndInvalidValues() {
        Slider newSlider = new Slider();
        newSlider.setSize(25); // Valid size
        newSlider.setX(10); // Valid X position
        newSlider.setY(15); // Valid Y position
        newSlider.setCurrentTravel(5); // Valid current travel
        newSlider.setMaxTravel(15); // Valid max travel
        newSlider.setMovementDirection(1); // Valid direction
        newSlider.setDirChangeCount(2); // Valid direction change count

        slider.update(newSlider);

        // Check that valid values were applied and invalid ones were not
        assertEquals(25, slider.getSize(), "Size should be updated to 25");
        assertEquals(10, slider.getx(), "X position should be updated to 10");
        assertEquals(15, slider.gety(), "Y position should be updated to 15");
        assertEquals(5, slider.getCurrentTravel(), "Current travel should be updated to 5");
        assertEquals(15, slider.getMaxTravel(), "Max travel should be updated to 15");
        assertEquals(1, slider.getMovementDirection(), "Movement direction should be updated to 1");
        assertEquals(2, slider.getDirChangeCount(), "Direction change count should be updated to 2");
    }
}
