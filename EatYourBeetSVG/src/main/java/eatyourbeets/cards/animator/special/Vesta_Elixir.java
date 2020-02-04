package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect;

import java.util.ArrayList;

public class Vesta_Elixir extends AnimatorCard
{
    public static final String ID = Register_Old(Vesta_Elixir.class);

    public final ArrayList<VestaElixirEffect> effects = new ArrayList<>();

    public Vesta_Elixir()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
    }

    public Vesta_Elixir(ArrayList<VestaElixirEffect> effects)
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

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
        return false;
    }

    @Override
    public void upgrade()
    {

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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