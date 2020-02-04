package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Zero extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register_Old(Zero.class);

    public Zero()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GrimoireOfZero);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard skill = JavaUtilities.GetRandomElement(p.drawPile.getSkills().group);
        if (skill != null)
        {
            p.drawPile.removeCard(skill);
            p.drawPile.addToTop(skill);
            skill.applyPowers();

            GameActions.Top.Add(new PlayTopCardAction(m, false));
        }
    }
}