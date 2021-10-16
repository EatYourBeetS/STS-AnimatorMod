package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.FreeAttackPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.UUID;

public class Iris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Iris.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public Iris()
    {
        super(DATA);

        Initialize(9, 0, 2);
        SetUpgrade(0,0,2);

        SetAffinity_Light(2);
        SetAffinity_Thunder();

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_BEAM_SHORT, 0.5f, 0.6f);
        GameActions.Bottom.BorderFlash(Color.YELLOW);
        GameActions.Bottom.VFX(VFX.Mindblast(player.dialogX, player.dialogY), 0.1f);
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE);

        boolean cardExhausted = false;

        for (AbstractCard c : p.discardPile.group) {

            if (GameUtilities.IsHindrance(c))
            {
                cardExhausted = true;
                GameActions.Bottom.Exhaust(c, p.discardPile);
            }
        }

        if (!cardExhausted)
        {
            GameActions.Bottom.StackPower(new FreeAttackPower(p, magicNumber));
        }
    }
}