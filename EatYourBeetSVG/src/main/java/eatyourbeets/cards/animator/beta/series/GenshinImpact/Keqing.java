package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEvokeOrbSubscriber;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.RandomizedList;

public class Keqing extends AnimatorCard implements OnEvokeOrbSubscriber
{
    public static final EYBCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage();

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 0);
        SetUpgrade(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Dark(0, 0, 1);


        SetRicochet(4, 0, this::OnCooldownCompleted);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CombatStats.onEvokeOrb.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()).duration);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER);

        AbstractCard card = null;
        RandomizedList<AbstractCard> possible = new RandomizedList<>();
        for (AbstractCard c : player.discardPile.group)
        {
            if (c.costForTurn >= 0)
            {
                possible.Add(c);
            }
        }
        if (possible.Size() > 0)
        {
            card = possible.Retrieve(rng);
        }

        ModifyCard(this);
        ModifyCard(card);
    }

    @Override
    public void OnEvokeOrb(AbstractOrb orb)
    {
        if ((Lightning.ORB_ID.equals(orb.ID) || Air.ORB_ID.equals(orb.ID)) && player.exhaustPile.contains(this))
        {
            cooldown.ProgressCooldownAndTrigger(null);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, true);
        CombatStats.onEvokeOrb.Unsubscribe(this);
    }

    private void ModifyCard(AbstractCard card){
        if (card != null) {
            if (card.cost > 0)
            {
                GameActions.Bottom.Motivate(card, 1);
            }
            GameActions.Bottom.ModifyTag(card, HASTE, true);
        }

    }
}