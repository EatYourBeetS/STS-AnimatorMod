package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.animatorClassic.special.MatouShinji_CommandSpell;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class MatouShinji extends AnimatorClassicCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final EYBCardData DATA = Register(MatouShinji.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.COMMON, EYBCardTarget.Random);
    static
    {
        DATA.AddPreview(new MatouShinji_CommandSpell(), false);
    }

    public MatouShinji()
    {
        super(DATA);

        Initialize(0, 2, 4);
        SetUpgrade(0, 0, 2);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block)
        .SetVFX(true, true);

        GameActions.Bottom.Callback(() ->
        {
            AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            if (GameUtilities.IsValidTarget(enemy))
            {
                GameActions.Top.ApplyPoison(player, enemy, magicNumber);
                GameActions.Top.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, enemy.hb.cX, enemy.hb.cY), 0.3f);
            }
        });

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new MatouShinji_CommandSpell())
            .AddCallback(c -> c.retain = true);
        }
    }
}
