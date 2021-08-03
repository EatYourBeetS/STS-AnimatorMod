package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.beta.special.Suigintou_BlackFeather;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;

public class Suigintou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Suigintou.class)
    		.SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Suigintou_BlackFeather(), false);
    }
    
    public Suigintou()
    {
        super(DATA);

        Initialize(4, 0, 3);
        SetUpgrade(2, 0, 1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(2, 0, 0);
        
        SetUnique(true, true);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Top.MakeCardInHand(new Suigintou_BlackFeather());

        super.triggerOnManualDiscard();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);

//        GameActions.Last.MoveCard(this, p.drawPile)
//                .ShowEffect(true, true)
//                .SetDestination(CardSelection.Bottom);

// it used to go to bottom of draw pile, with megu drawing from bottom

        cooldown.ProgressCooldownAndTrigger(m);
    }
    
    protected void OnCooldownCompleted(AbstractMonster m)
    {
        AbstractOrb fireOrb=new Fire();
    	GameActions.Bottom.ChannelOrb(fireOrb);

    	for (int i = 0; i < magicNumber; i++)
        {
            fireOrb.onStartOfTurn();
            fireOrb.onEndOfTurn();
        }
    }
}
