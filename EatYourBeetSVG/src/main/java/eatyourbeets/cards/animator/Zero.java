package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Zero extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Zero.class.getSimpleName(), EYBCardBadge.Synergy);

    public Zero()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0);

        this.exhaust = true;
        SetSynergy(Synergies.GrimoireOfZero);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard skill = Utilities.GetRandomElement(p.drawPile.getSkills().group);
        if (skill != null)
        {
            p.drawPile.removeCard(skill);
            p.drawPile.addToTop(skill);
            skill.applyPowers();
            GameActionsHelper.AddToTop(new PlayTopCardAction(m, false));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.exhaust = false;
        }
    }
}