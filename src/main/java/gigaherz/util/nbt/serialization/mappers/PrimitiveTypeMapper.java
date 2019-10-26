package gigaherz.util.nbt.serialization.mappers;

import com.google.common.primitives.Primitives;
import net.minecraft.nbt.CompoundNBT;

public class PrimitiveTypeMapper extends MapperBase
{
    public PrimitiveTypeMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return clazz.isPrimitive() || Primitives.isWrapperType(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return false;
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException
    {
        if (object instanceof Byte)
        {
            parent.putByte(fieldName, (Byte) object);
        }
        else if (object instanceof Short)
        {
            parent.putShort(fieldName, (Short) object);
        }
        else if (object instanceof Integer)
        {
            parent.putInt(fieldName, (Integer) object);
        }
        else if (object instanceof Long)
        {
            parent.putLong(fieldName, (Long) object);
        }
        else if (object instanceof Float)
        {
            parent.putFloat(fieldName, (Float) object);
        }
        else if (object instanceof Double)
        {
            parent.putDouble(fieldName, (Double) object);
        }
        else if (object instanceof Boolean)
        {
            parent.putBoolean(fieldName, (Boolean) object);
        }
        else if (object instanceof Character)
        {
            parent.putInt(fieldName, (Character) object);
        }
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException
    {
        if (clazz == Byte.class || clazz == byte.class)
        {
            return parent.getByte(fieldName);
        }
        else if (clazz == Short.class || clazz == short.class)
        {
            return parent.getShort(fieldName);
        }
        else if (clazz == Integer.class || clazz == int.class)
        {
            return parent.getInt(fieldName);
        }
        else if (clazz == Long.class || clazz == long.class)
        {
            return parent.getLong(fieldName);
        }
        else if (clazz == Float.class || clazz == float.class)
        {
            return parent.getFloat(fieldName);
        }
        else if (clazz == Double.class || clazz == double.class)
        {
            return parent.getDouble(fieldName);
        }
        else if (clazz == Boolean.class || clazz == boolean.class)
        {
            return parent.getBoolean(fieldName);
        }
        else if (clazz == Character.class || clazz == char.class)
        {
            return parent.getInt(fieldName);
        }
        return null;
    }

    @Override
    public CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException
    {
        return null;
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz) throws ReflectiveOperationException
    {
        return null;
    }
}
