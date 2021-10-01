package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.animator.special.MatouShinji_CommandSpell;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class MatouShinji extends AnimatorCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final EYBCardData DATA = Register(MatouShinji.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MatouShinji_CommandSpell(), false));

    public MatouShinji()
    {
        super(DATA);

        Initialize(0, 3, 4, 5);
        SetUpgrade(0, 0, 2);

        SetAffinity_Orange(1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block)
        .SetVFX(true, true);

        GameActions.Bottom.Callback(() ->
        {
            final AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            if (GameUtilities.IsValidTarget(enemy))
            {
                GameActions.Top.ApplyPoison(player, enemy, magicNumber);
                GameActions.Top.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, enemy.hb.cX, enemy.hb.cY), 0.3f);
            }
        });

        if (info.IsSynergizing  && info.PreviousCard != null && GameUtilities.IsHighCost(info.PreviousCard) && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.DARK);
            GameActions.Bottom.MakeCardInHand(new MatouShinji_CommandSpell());
        }
    }
}
