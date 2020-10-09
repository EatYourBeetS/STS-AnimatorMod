package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.util.HashMap;
import java.util.Map;

public class EYBCombatScreen extends GUIElement
{
    protected final static FieldInfo<BobEffect> _bobEffect = JUtils.GetField("bobEffect", AbstractMonster.class);
    protected final static FieldInfo<Boolean> _isMultiDmg = JUtils.GetField("isMultiDmg", AbstractMonster.class);
    protected final static FieldInfo<Integer> _intentMultiAmt = JUtils.GetField("intentMultiAmt", AbstractMonster.class);

    protected final Map<AbstractMonster, Integer> intents = new HashMap<>();

    public void AddSubIntent(AbstractMonster monster, int damage)
    {
        intents.put(monster, damage);
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
            RenderSubIntent(sb, m, m.intent, intents.get(m));
            intents.remove(m);
        }
    }

    private void RenderSubIntent(SpriteBatch sb, AbstractMonster m, AbstractMonster.Intent intent, int damage)
    {
        final boolean multiDamage = _isMultiDmg.Get(m);
        final int multiDamageAmount = _intentMultiAmt.Get(m);
        final BitmapFont font = EYBFontHelper.CardDescriptionFont_Normal;
        final Color color = Settings.GREEN_TEXT_COLOR.cpy().lerp(Color.WHITE, 0.2f);
        color.a = m.intentAlpha;

        if (multiDamage)
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage + "x" + multiDamageAmount,
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, font, Integer.toString(damage),
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }

        RenderHelpers.ResetFont(font);
    }
}
