package us.core_network.cornel.java;

/**
 * Class to convert between primitive and wrapper number arrays.
 */
public class ArrayConvert {

    /**
     * Utility method that converts array of {@link java.lang.Integer} into array of primitive <code>int</code>.
     */
	public static int[] convert(Integer[] input)
	{
		int[] array = new int[input.length];
		for (int i = 0; i < array.length; i++)
			array[i] = input[i].intValue();
		
		return array;
	}

    /**
     * Utility method that converts array of {@link java.lang.Byte} into array of primitive <code>byte</code>.
     */
	public static byte[] convert(Byte[] input)
	{
		byte[] array = new byte[input.length];
		for (int i = 0; i < array.length; i++)
			array[i] = input[i].byteValue();
		
		return array;
	}

    /**
     * Utility method that converts array of primitive <code>int</code> into array of {@link java.lang.Integer}.
     */
	public static Integer[] convert(int[] input)
	{
		Integer[] array = new Integer[input.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Integer.valueOf(input[i]);
		
		return array;
	}

    /**
     * Utility method that converts array of primitive <code>byte</code> into array of {@link java.lang.Byte}.
     */
    public static Byte[] convert(byte[] input)
	{
		Byte[] array = new Byte[input.length];
		for (int i = 0; i < array.length; i++)
			array[i] = Byte.valueOf(input[i]);
		
		return array;
	}
	
}
