package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.TenseiSlime.Vesta;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect;
import eatyourbeets.resources.GR;

import java.util.ArrayList;

public class Vesta_Elixir extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vesta_Elixir.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Vesta.DATA.Series);

    public final ArrayList<VestaElixirEffect> effects = new ArrayList<>();

    public Vesta_Elixir()
    {
        super(DATA);

        tags.add(GR.Enums.CardTags.TEMPORARY);

        SetAffinity_Star(1);
    }

    public Vesta_Elixir(ArrayList<VestaElixirEffect> effects)
    {
        this();

        ApplyEffects(effects);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Vesta_Elixir other = (Vesta_Elixir) super.makeStatEquivalentCopy();

        other.ApplyEffects(effects);

        return other;
    }

    @Override
    public boolean canUpgrade()
    {
        return effects.isEmpty();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (VestaElixirEffect effect : effects)
        {
            effect.EnqueueAction(this, p);
        }
    }

    public void ApplyEffect(VestaElixirEffect effect)
    {
        this.effects.add(effect);
        this.cardText.OverrideDescription(this.rawDescription + " NL " + effect.GetDescription(), true);
    }

    public void ApplyEffects(ArrayList<VestaElixirEffect> effects)
    {
        this.effects.clear();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < effects.size(); i++)
        {
            VestaElixirEffect effect = effects.get(i);

            this.effects.add(effect);

            String desc = effect.GetDescription();
            if (desc != null && !desc.isEmpty())
            {
                sb.append(effect.GetDescription());

                if (i < effects.size() - 1)
                {
                    sb.append(" NL ");
                }
            }
        }

        this.cardText.OverrideDescription(sb.toString(), true);
    }
}