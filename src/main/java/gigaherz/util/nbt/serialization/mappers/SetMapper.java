package gigaherz.util.nbt.serialization.mappers;

import gigaherz.util.nbt.serialization.NBTSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.SerializationException;

import java.util.Set;

@SuppressWarnings("unchecked")
public class SetMapper extends MapperBase
{
    public SetMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return Set.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return Set.class.isAssignableFrom(clazz);
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        parent.put(fieldName, serializeSet((Set) object));
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeSet(tag2, (Class<? extends Set>) clazz);
    }

    @Override
    public CompoundNBT serializeCompound(Object object)
            throws ReflectiveOperationException
    {
        return serializeSet((Set) object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz)
            throws ReflectiveOperationException
    {
        return deserializeSet(self, (Class<? extends Set>) clazz);
    }

    private CompoundNBT serializeSet(Set s)
            throws ReflectiveOperationException
    {
        CompoundNBT tag = getTypeCompound(s, "set");

        ListNBT list = new ListNBT();
        for (Object o : s)
        {
            CompoundNBT tag2 = new CompoundNBT();
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

    private Set deserializeSet(CompoundNBT tag, Class<? extends Set> clazz)
            throws ReflectiveOperationException
    {
        if (!tag.getString("type").equals("set"))
            throw new SerializationException();

        Class<?> actual = Class.forName(tag.getString("className"));
        if (!clazz.isAssignableFrom(actual))
            throw new SerializationException();

        Set s = (Set) actual.newInstance();

        ListNBT list = tag.getList("elements", Constants.NBT.TAG_COMPOUND);
        for (int ii = 0; ii < list.size(); ii++)
        {
            CompoundNBT tag2 = (CompoundNBT) list.get(ii);

            Object value = null;
            if (tag2.contains("value"))
            {
                Class<?> cls = Class.forName(tag2.getString("valueClass"));
                value = NBTSerializer.deserializeFrom(tag2, "value", cls, null);
            }

            s.add(value);
        }

        return s;
    }

}
