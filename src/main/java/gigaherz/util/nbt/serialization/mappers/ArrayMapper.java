package gigaherz.util.nbt.serialization.mappers;

import gigaherz.util.nbt.serialization.NBTSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.SerializationException;

import java.lang.reflect.Array;

public class ArrayMapper extends MapperBase
{
    public ArrayMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return clazz.isArray();
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return clazz.isArray();
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        parent.put(fieldName, serializeArray(object));
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeArray(tag2, clazz);
    }

    @Override
    public CompoundNBT serializeCompound(Object object)
            throws ReflectiveOperationException
    {
        return serializeArray(object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz)
            throws ReflectiveOperationException
    {
        return deserializeArray(self, clazz);
    }

    private CompoundNBT serializeArray(Object a)
            throws ReflectiveOperationException
    {
        CompoundNBT tag = getTypeCompound(a, "array");

        ListNBT list = new ListNBT();
        for (int ii = 0; ii < Array.getLength(a); ii++)
        {
            Object o = Array.get(a, ii);
            CompoundNBT tag2 = new CompoundNBT();
            tag2.putInt("index", ii);
            if (o != null)
            {
                NBTSerializer.serializeTo(tag2, "valueClass", o.getClass().getName());
                NBTSerializer.serializeTo(tag2, "value", o);
            }
            list.add(tag2);
        }
        tag.put("elements", list);
        return tag;
    }

    private Object deserializeArray(CompoundNBT tag, Class<?> clazz)
            throws ReflectiveOperationException
    {
        if (!tag.getString("type").equals("array"))
            throw new SerializationException();

        ListNBT list = tag.getList("elements", Constants.NBT.TAG_COMPOUND);

        Object o = Array.newInstance(clazz.getComponentType(), list.size());

        for (int ii = 0; ii < list.size(); ii++)
        {
            CompoundNBT tag2 = (CompoundNBT) list.get(ii);

            int index = tag2.getInt("index");

            if (!tag2.contains("value"))
            {
                continue;
            }

            Class<?> cls = Class.forName(tag2.getString("valueClass"));
            Object value = NBTSerializer.deserializeFrom(tag2, "value", cls, null);

            if (cls == Byte.class || cls == byte.class)
            {
                Array.setByte(o, index, (Byte) value);
            }
            else if (cls == Short.class || cls == short.class)
            {
                Array.setShort(o, index, (Short) value);
            }
            else if (cls == Integer.class || cls == int.class)
            {
                Array.setInt(o, index, (Integer) value);
            }
            else if (cls == Long.class || cls == long.class)
            {
                Array.setLong(o, index, (Long) value);
            }
            else if (cls == Float.class || cls == float.class)
            {
                Array.setFloat(o, index, (Float) value);
            }
            else if (cls == Double.class || cls == double.class)
            {
                Array.setDouble(o, index, (Double) value);
            }
            else if (cls == Boolean.class || cls == boolean.class)
            {
                Array.setBoolean(o, index, (Boolean) value);
            }
            else if (cls == Character.class || cls == char.class)
            {
                Array.setChar(o, index, (Character) value);
            }
            else
            {
                Array.set(o, index, value);
            }
        }

        return o;
    }
}
