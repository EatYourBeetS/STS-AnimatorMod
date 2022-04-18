package eatyourbeets.ui.animator.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class EnemySubIntents
{
    protected final static FieldInfo<BobEffect> _bobEffect = JUtils.GetField("bobEffect", AbstractMonster.class);
    protected final static FieldInfo<Boolean> _isMultiDmg = JUtils.GetField("isMultiDmg", AbstractMonster.class);
    protected final static FieldInfo<Integer> _intentMultiAmt = JUtils.GetField("intentMultiAmt", AbstractMonster.class);
    protected final ArrayList<EnemyIntent> intents = new ArrayList<>();

    public void Add(EnemyIntent intent)
    {
        if (!intents.contains(intent))
        {
            intents.add(intent);
        }
    }

    public void Clear()
    {
        for (EnemyIntent intent : intents)
        {
            intent.modifiers.clear();
        }

        intents.clear();
    }

    public void RenderMonsterInfo(AbstractMonster m, SpriteBatch sb)
    {
        for (int i = 0; i < intents.size(); i++)
        {
            final EnemyIntent intent = intents.get(i);
            if (intent.enemy == m)
            {
                RenderSubIntent(sb, m, intent.CreateIntentDamageString());
                intent.modifiers.clear();
                intents.remove(i);
                return;
            }
        }
    }

    private void RenderSubIntent(SpriteBatch sb, AbstractMonster m, ColoredString damage)
    {
        if (damage == null || damage.color == null)
        {
            return;
        }

        final boolean multiDamage = _isMultiDmg.Get(m);
        final int multiDamageAmount = _intentMultiAmt.Get(m);
        final BitmapFont font = EYBFontHelper.CardDescriptionFont_Large;
        final Color color = new Color(damage.color.r, damage.color.g, damage.color.b, m.intentAlpha);

        if (multiDamage)
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage.text + "x" + multiDamageAmount,
            m.intentHb.x + (Settings.scale * 5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage.text,
            m.intentHb.x + (Settings.scale * 5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }

        RenderHelpers.ResetFont(font);
    }
}
