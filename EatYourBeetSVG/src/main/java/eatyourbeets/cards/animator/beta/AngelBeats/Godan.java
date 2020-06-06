package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Godan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Godan.class).SetAttack(1, CardRarity.UNCOMMON);

    public Godan()
    {
        super(DATA);

        Initialize(5, 0, 2);
        SetUpgrade(1, 0, 1);
        SetScaling(0, 0, 1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.AngelBeats);
        SetMartialArtist();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
    }
}