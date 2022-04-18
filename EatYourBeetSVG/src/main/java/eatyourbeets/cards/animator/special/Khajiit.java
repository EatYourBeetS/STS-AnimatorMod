package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Khajiit extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Khajiit.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Overlord)
            .SetMaxCopies(1)
            .PostInitialize(data -> data.AddPreview(new Khajiit_SkeletalDragon(), true));

    private static AbstractCard preview;
    private static AbstractCard preview_upgraded;

    public Khajiit()
    {
        super(DATA);

        Initialize(0, 0, 4, 1);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Dark(1);
        SetAffinity_Blue(1);

        SetCardPreview((cards, m) ->
        {
            if (preview == null)
            {
                preview = Khajiit_SkeletalDragon.DATA.MakeCopy(false);
                preview_upgraded = Khajiit_SkeletalDragon.DATA.MakeCopy(false);
            }

            final AbstractCard toAdd = upgraded ? preview_upgraded : preview;
            toAdd.calculateCardDamage(m);
            cards.Clear();
            cards.Add(toAdd);
        });
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (CheckSpecialCondition(false))
        {
            SetAttackTarget(EYBCardTarget.Normal);
            cardPreview.SetEnabled(true);
        }
        else
        {
            SetAttackTarget(EYBCardTarget.None);
            cardPreview.SetEnabled(false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.GainCorruption(1, upgraded);
        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(c -> Dark.ORB_ID.equals(c.ID))
        .SetSequential(true);

        if (CheckSpecialCondition(true))
        {
            Khajiit_SkeletalDragon.DATA.MarkSeen();
            GameActions.Bottom.PlayCard(new Khajiit_SkeletalDragon(this), m);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        if (CombatStats.CanActivateLimited(cardID))
        {
            int curses = 0;
            for (AbstractCard c : player.exhaustPile.group)
            {
                if (c.type == CardType.CURSE && (curses += 1) >= 3)
                {
                    return !tryUse || CombatStats.TryActivateLimited(cardID);
                }
            }
        }

        return false;
    }
}