package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.animator.special.MatouShinji_CommandSpell;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class MatouShinji extends AnimatorCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final EYBCardData DATA = Register(MatouShinji.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new MatouShinji_CommandSpell(), false);
    }

    public MatouShinji()
    {
        super(DATA);

        Initialize(0, 3, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

        if (isSynergizing && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new MatouShinji_CommandSpell());
        }
    }
}
