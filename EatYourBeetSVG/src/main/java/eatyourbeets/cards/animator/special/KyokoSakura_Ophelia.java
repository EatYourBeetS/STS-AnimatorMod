package eatyourbeets.cards.animator.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.KyokoSakura;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class KyokoSakura_Ophelia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura_Ophelia.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental)
            .SetSeries(KyokoSakura.DATA.Series);

    public KyokoSakura_Ophelia()
    {
        super(DATA);

        Initialize(8, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Dark(1);
        SetAffinity_Red(2);
        SetAffinity_Blue(0, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.ChannelOrbs(Fire::new, secondaryValue);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.ModifyAllInstances(uuid).AddCallback(c -> GameUtilities.IncreaseMagicNumber(c, 1, false));
        GameActions.Last.Callback(() ->
        {
            if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
            {
                GameActions.Bottom.Wait(GameEffects.List.ShowCopy(this, Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.5f).duration * 0.35f);
                GameActions.Bottom.Motivate(2);
                GameActions.Top.Exhaust(this);
            }
        });
    }
}