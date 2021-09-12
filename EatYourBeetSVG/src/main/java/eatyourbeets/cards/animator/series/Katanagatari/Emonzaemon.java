package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Emonzaemon_EntouJyuu;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Emonzaemon extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Emonzaemon.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Emonzaemon_EntouJyuu(), true));

    public Emonzaemon()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.55f, 0.65f);
        GameActions.Bottom.WaitRealtime(0.25f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.55f, 0.65f);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.MakeCardInDrawPile(new Emonzaemon_EntouJyuu())
            .SetDestination(CardSelection.Random)
            .SetUpgrade(upgraded, false);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        if (CombatStats.CanActivateLimited(cardID))
        {
            final ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            final int size = cardsPlayed.size();
            final int count = tryUse ? 3 : 2;
            if (size >= count)
            {
                boolean canActivate = true;
                for (int i = 1; i <= count; i++)
                {
                    if (cardsPlayed.get(size - i).type != CardType.ATTACK)
                    {
                        canActivate = false;
                    }
                }

                if (canActivate)
                {
                    return !tryUse || CombatStats.TryActivateLimited(cardID);
                }
            }
        }

        return false;
    }
}