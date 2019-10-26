package gigaherz.util.nbt.serialization.mappers;

import gigaherz.util.nbt.serialization.NBTSerializer;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.SerializationException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GenericObjectMapper extends MapperBase
{
    public GenericObjectMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return true;
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return true;
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException
    {
        CompoundNBT tag2 = serializeObject(object);
        parent.put(fieldName, tag2);
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeObject(tag2, clazz);
    }

    @Override
    public CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException
    {
        return serializeObject(object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz) throws ReflectiveOperationException
    {
        return deserializeObject(self, clazz);
    }

    private CompoundNBT serializeObject(Object o)
            throws ReflectiveOperationException
    {
        if (o == null)
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("type", "null");
            return tag;
        }

        CompoundNBT tag = getTypeCompound(o, "object");

        Class<?> cls = o.getClass();

        // The loop skips Object
        while (cls.getSuperclass() != null)
        {
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields)
            {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;

                f.setAccessible(true);
                NBTSerializer.serializeTo(tag, f.getName(), f.get(o));
            }

            cls = cls.getSuperclass();
        }

        return tag;
    }

    private Object deserializeObject(CompoundNBT tag, Class<?> clazz)
            throws ReflectiveOperationException
    {
        if (tag.getString("type").equals("null"))
            return null;

        if (!tag.getString("type").equals("object"))
            throw new SerializationException();

        Class<?> actual = Class.forName(tag.getString("className"));
        if (!clazz.isAssignableFrom(actual))
            throw new SerializationException();

        Class<?> cls = actual;

        Object o = cls.newInstance();

        // The loop skips Object
        while (cls.getSuperclass() != null)
        {
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields)
            {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;

                f.setAccessible(true);
                f.set(o, NBTSerializer.deserializeFrom(tag, f.getName(), f.getType(), f.get(o)));
            }

            cls = cls.getSuperclass();
        }

        return o;
    }
}
