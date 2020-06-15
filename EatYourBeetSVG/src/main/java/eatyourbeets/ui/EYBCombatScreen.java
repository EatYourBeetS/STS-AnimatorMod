package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.*;

import java.util.HashMap;
import java.util.Map;

public class EYBCombatScreen extends GUIElement
{
    protected final static FieldInfo<BobEffect> _bobEffect = JUtils.GetField("bobEffect", AbstractMonster.class);
    protected final static FieldInfo<Boolean> _isMultiDmg = JUtils.GetField("isMultiDmg", AbstractMonster.class);
    protected final static FieldInfo<Integer> _intentMultiAmt = JUtils.GetField("intentMultiAmt", AbstractMonster.class);

    protected final Map<AbstractMonster, FuncT0<ColoredString>> intents = new HashMap<>();

    public void AddSubIntent(AbstractMonster monster, FuncT0<ColoredString> calculateIntent)
    {
        intents.put(monster, calculateIntent);
    }

    @Override
    public void Update()
    {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        throw new RuntimeException("Not Implemented");
    }

    public void Render(AbstractMonster m, SpriteBatch sb)
    {
        if (intents.containsKey(m))
        {
            RenderSubIntent(sb, m, m.intent, intents.get(m).Invoke());
            intents.remove(m);
        }
    }

    private void RenderSubIntent(SpriteBatch sb, AbstractMonster m, AbstractMonster.Intent intent, ColoredString damage)
    {
        final boolean multiDamage = _isMultiDmg.Get(m);
        final int multiDamageAmount = _intentMultiAmt.Get(m);
        final BitmapFont font = EYBFontHelper.CardDescriptionFont_Normal;
        final Color color = damage.color.cpy();
        color.a = m.intentAlpha;

        if (multiDamage)
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage + "x" + multiDamageAmount,
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage.text,
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }

        RenderHelpers.ResetFont(font);
    }
}
