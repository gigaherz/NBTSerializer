package gigaherz.util.nbt.serialization.mappers;

import gigaherz.util.nbt.serialization.NBTSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.SerializationException;

import java.util.List;

@SuppressWarnings("unchecked")
public class ListMapper extends MapperBase
{
    public ListMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        parent.put(fieldName, serializeList((List) object));
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeList(tag2, (Class<? extends List>) clazz);
    }

    @Override
    public CompoundNBT serializeCompound(Object object)
            throws ReflectiveOperationException
    {
        return serializeList((List) object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz)
            throws ReflectiveOperationException
    {
        return deserializeList(self, (Class<? extends List>) clazz);
    }

    private CompoundNBT serializeList(List l)
            throws ReflectiveOperationException
    {
        CompoundNBT tag = getTypeCompound(l, "list");

        ListNBT list = new ListNBT();
        for (int ii = 0; ii < l.size(); ii++)
        {
            Object o = l.get(ii);
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

    private List deserializeList(CompoundNBT tag, Class<? extends List> clazz)
            throws ReflectiveOperationException
    {
        if (!tag.getString("type").equals("list"))
            throw new SerializationException();

        Class<?> actual = Class.forName(tag.getString("className"));
        if (!clazz.isAssignableFrom(actual))
            throw new SerializationException();

        List l = (List) actual.newInstance();

        ListNBT list = tag.getList("elements", Constants.NBT.TAG_COMPOUND);

        for (int ii = 0; ii < list.size(); ii++)
        {
            l.add(null);
        }

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

            l.set(index, value);
        }

        return l;
    }
}
