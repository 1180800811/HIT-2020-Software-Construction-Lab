package applications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import Location.Location;
import MyException.cancelPlanningEntryException;
import MyException.deleteLocationException;
import MyException.deleteResourceException;
import MyException.feipeiResourceException;
import PlanningEntry.*;

public class FlightScheduleTest {
	/*
	 * Testing for addPlanningEntry����
	 * Partition : there is conflict after adding a PlanningEntry , there is not conflict after adding a PlanningEntry 
	 * 
	 * Testing for cancelPlaningEntry����
	 * Partition : the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 * 
	 * Testing for FeiPeiResource
	 * output:0 : ��Ҫ����ķɻ����ڿ��÷ɻ��� , 1 : ��Ҫ������Դ�ļƻ����Ѿ�������Դ 2 :����ɹ� , 3 :������Դ���ڳ�ͻ , 4:��Ҫ����ɻ��ĺ��಻����
	 *
	 *	Testing for BeginPlanningEntry����
	 *	Partition : the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 *
	 * Testing for EndPlanningEntry����
	 *  Partition��the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 *  
	 *	Testing for addLocation����
	 *	Partition : ���ӵ�λ�ò����ڣ����ӵ�λ���Ѿ�����
	 *
	 *	Testing for deleteLocation����
	 *	Partition : ��ɾ����λ�ò����ڣ���ɾ����λ���Ѿ�����
	 *
	 *	Testing for addResource����
	 *	Partition : ���ӵ���Դ�����ڣ����ӵ���Դ�Ѿ�����
	 *
	 *	Testing for deleteResource����
	 *	Partition : ��ɾ������Դ�����ڣ���ɾ������Դ�Ѿ�����
	 *
	 * Testing for check ����
	 * Partition:there is conflict between PlanningEntrys , there is not conflict between PlanningEntrys , 
	 * 
	 * Testing for getPrePlanningEntry ����
	 * Partition: there is PrePlanningEntry , there is not PrePlanningEntry
	 */
	
	/*
	 * ����addPlanningEntry����
	 * ���ǣ�there is conflict after adding a PlanningEntry , there is not conflict after adding a PlanningEntry 
	 */
	
	@Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
	@Test
	public void addPlanningEntryTest() {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule f = new FlightSchedule();
		assertTrue(f.addPlanningEntry(loc1, loc2, time1, time2, name));	// there is not conflict after adding a PlanningEntry 	
	}
	
	/*
	 * 	���� cancelPlaningEntry����
	 * ���ǣ�the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 */
	@Test
	public void cancelPlaningEntryTest() throws cancelPlanningEntryException {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule f = new FlightSchedule();
		f.addPlanningEntry(loc1, loc2, time1, time2, name);
		assertTrue(f.cancelPlanningEntry(name, time1, time2).equals("�����Ѿ�ȡ��"));
		Calendar time3 =Calendar.getInstance() ;
		time3.set(2020, 5-1, 6, 10, 22);
		f.setTime(time3);
		try {
			f.cancelPlanningEntry(name, time1, time2);
		}catch(cancelPlanningEntryException e ) {
			assertEquals("ȡ���ƻ����쳣: �ƻ���ss��ǰ��״̬������ȡ��",e.getMessage());
		}

		
	}
	/*
	 * ����FeiPeiResource����
	 * ����:0 : ��Ҫ����ķɻ����ڿ��÷ɻ��� , 1 : ��Ҫ������Դ�ļƻ����Ѿ�������Դ 2 :����ɹ� , 3 :������Դ���ڳ�ͻ , 4:��Ҫ����ɻ��ĺ��಻����
	 */
	@Test
	public void FeiPeiResourceTest() throws Exception {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		
		FlightSchedule f = new FlightSchedule();
		f.addPlanningEntry(loc1, loc2, time1, time2, name);
		assertTrue(f.FeiPeiResource("tt", time1, time2, name) == 0 );//����:��Ҫ����ķɻ����ڿ��÷ɻ���
		f.addResource("tt", "ss", 400, 10.0);//���ӿ��õķɻ�
		assertTrue(f.FeiPeiResource("tt", time1, time2, "tt") == 4 );//����:��Ҫ����ɻ��ĺ��಻����
		assertTrue(f.FeiPeiResource("tt", time1, time2, name) == 2) ;//����:����ɹ�
		assertTrue(f.FeiPeiResource("tt", time1, time2, name) == 1) ;//����:��Ҫ������Դ�ļƻ����Ѿ�������Դ
		Calendar time11 = Calendar.getInstance() ;
		time11.set(2020, 2-1, 13, 14, 30);
		Calendar time22 = Calendar.getInstance() ;
		time22.set(2020, 2-1, 13, 17, 30);
		Location loc11 = new Location(11, 22, "��������", true);
		Location loc22 = new Location(11, 22, "������", true);
		String name1 = "ss" ;
		f.addPlanningEntry(loc11, loc22, time11, time22, name1);
		try {
			f.FeiPeiResource("tt", time11, time22, name1);//����:������Դ���ڳ�ͻ
		}catch(feipeiResourceException e) {
			System.out.println(e.getMessage());
			assertEquals("�������Դ:PlanningEntry.FlightEntry@6659c656�󣬼ƻ���֮�������Դ��ռ��ͻ" ,e.getMessage());
		}

	}
	/*
	 * ����BeginPlanningEntry����
	 * ���ǣ�the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 */
	@Test
	public void BeginPlanningEntryTest() throws Exception {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule g = new FlightSchedule();


		assertTrue(g.BeginPlanningEntry(name, time1, time2).equals("ָ���ĺ��಻����"));//����:ָ���ĺ��಻����
		g.addPlanningEntry(loc1, loc2, time1, time2, name);
		assertTrue(g.BeginPlanningEntry(name, time1, time2).equals("���൱ǰ״̬ΪWAITTING,��������"));//����:ָ���ĺ��಻����
		g.addResource("tt", "ss", 400, 10.0);//���ӿ��õķɻ�
		g.FeiPeiResource("tt", time1, time2, name);
		assertTrue(g.BeginPlanningEntry(name, time1, time2).equals("�����Ѿ�����"));
	}
	
	/*
	 * ����EndPlanningEntry����
	 * ���ǣ�the state of the PlanningEntry is ALOCATED, WAITING,CANCELLED,ENDED,RUNNING
	 */
	@Test
	public void EndPlanningEntryTest() throws Exception {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule g = new FlightSchedule();
		g.addPlanningEntry(loc1, loc2, time1, time2, name);
		g.addResource("tt", "ss", 400, 10.0);//���ӿ��õķɻ�
		g.FeiPeiResource("tt", time1, time2, name);
		assertTrue(g.EndPlanningEntry("sss", time1, time2).equals("ָ���ĺ��಻����"));
		assertTrue(g.EndPlanningEntry(name, time1, time2).equals("���൱ǰ״̬ΪALOCATED,���ܽ���"));
		assertTrue(g.BeginPlanningEntry(name, time1, time2).equals("�����Ѿ�����"));
		assertTrue(g.EndPlanningEntry(name, time1, time2).equals("�����Ѿ�����"));
	}
	/*
	 * ����addLocation����
	 */
	@Test
	public void addLocationTest() throws Exception {

		FlightSchedule c = new FlightSchedule() ;
		assertTrue(c.addLocation(new Location("ss")));//����λ�óɹ�
		assertFalse(c.addLocation(new Location("ss")));//���ӵ�λ���Ѿ�����
		c.showLocation();
	}
	
	/*
	 * 
	 * ����deletLocation����
	 */
	@Test
	public void deleteLocationTest() throws deleteLocationException, feipeiResourceException {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		FlightSchedule c = new FlightSchedule() ;
		assertFalse(c.deleteLocation(new Location("ss")));//��ɾ����λ�ò�����
		c.addLocation(new Location("ss"));
		assertTrue(c.deleteLocation(new Location("ss")));//��ɾ����λ�ô���
		c.addLocation(new Location("ss"));
		c.addPlanningEntry(new Location("ss"), new Location("t"), time1, time2, "s");
		c.addResource("s", "s", 400, 16);
		c.FeiPeiResource("s", time1, time2, "s");
		c.BeginPlanningEntry("s", time1, time2);
		try {
			c.deleteLocation(new Location("ss"));
		}catch(deleteLocationException e) {
			assertEquals("ɾ��λ���쳣: ������δ�����ļƻ�����λ��:ssִ��",e.getMessage());
		}

	}
	/*
	 * ����addResource����
	 */
	@Test
	public void addResourceTest() throws Exception {

		FlightSchedule c = new FlightSchedule("test/applications/FlightScheduel_2") ;
		assertTrue(c.addResource("ss", "ss", 400, 10.0));//������Դ�ɹ�
		assertFalse(c.addResource("ss", "ss", 400, 10.0));//���ӵ���Դ�Ѿ�����
		c.showResource();
	}
	/*
	 * 
	 * ����deletResource����
	 */
	@Test
	public void deleteResourceTest() throws deleteResourceException, feipeiResourceException, deleteLocationException {
		FlightSchedule c = new FlightSchedule();
		assertFalse(c.deleteResource("ss", "ss",400, 10.0));//��ɾ������Դ������
		c.addResource("ss", "ss", 400, 10.0);
		assertTrue(c.deleteResource("ss", "ss", 400, 10.0));//��ɾ������Դ����
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		c.addLocation(new Location("ss"));
		c.addPlanningEntry(new Location("ss"), new Location("t"), time1, time2, "s");
		c.addResource("s", "s", 400, 16);
		c.FeiPeiResource("s", time1, time2, "s");
		c.BeginPlanningEntry("s", time1, time2);
		try {
			c.deleteResource("s", "s", 400, 16);
		}catch(deleteResourceException e) {
			assertEquals("ɾ����Դ�쳣: ����δ�����ļƻ�������ʹ�ø���Դ:Plane [PlaneNumber=s, MachineNumber=s, Size=400, Age=16.0]",e.getMessage());
		}

	}
	
	
	/*
	 * ����check����
	 */
	@Test
	public void checkTest() throws Exception {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule g = new FlightSchedule();
		g.addPlanningEntry(loc1, loc2, time1, time2, name);
		g.addResource("tt", "ss", 400, 10.0);//���ӿ��õķɻ�
		g.FeiPeiResource("tt", time1, time2, name);
		assertTrue(g.check());
	}
	/*
	 * ����getPrePlanningEntry����
	 */
	@Test
	public void getPrePlanningEntryTest() throws Exception {
		Calendar time1 = Calendar.getInstance() ;
		time1.set(2020, 2-1, 13, 14, 30);
		Calendar time2 = Calendar.getInstance() ;
		time2.set(2020, 2-1, 13, 17, 30);
		Location loc1 = new Location(11, 22, "��������", true);
		Location loc2 = new Location(11, 22, "������", true);
		String name = "ss" ;
		FlightSchedule g = new FlightSchedule();
		g.addPlanningEntry(loc1, loc2, time1, time2, name);
		g.addResource("tt", "ss", 400, 10.0);//���ӿ��õķɻ�
		g.FeiPeiResource("tt", time1, time2, name);
		assertFalse(g.getPrePlanningEntry(time1,time2,name,"tt"));
		
		Calendar time11 = Calendar.getInstance() ;
		time11.set(2020, 2-1, 12, 14, 30);
		Calendar time22 = Calendar.getInstance() ;
		time22.set(2020, 2-1, 12, 17, 30);
		Location loc11 = new Location(11, 22, "��������", true);
		Location loc22 = new Location(11, 22, "������", true);
		String name1 = "ss" ;
		g.addPlanningEntry(loc11, loc22, time11, time22, name1);
		g.FeiPeiResource("tt", time11, time22, name1);
		assertTrue(g.getPrePlanningEntry(time1,time2,name,"tt"));
		g.getResourcePlanningEntry("tt", time11);
		g.showFlightEntry(loc11, time1);
		g.WatchState("ss", time11, time22);
		g.show(loc11, time11);
		g.board(loc11, time11, 0);
	}
}