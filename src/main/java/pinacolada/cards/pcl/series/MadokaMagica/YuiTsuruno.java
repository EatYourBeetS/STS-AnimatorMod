package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.CardSelection;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.effects.AttackEffects;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;

public class YuiTsuruno extends PCLCard
{
    public static final PCLCardData DATA = Register(YuiTsuruno.class)
            .SetAttack(0, CardRarity.COMMON, PCLAttackType.Fire, PCLCardTarget.Random)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(5, 0);
        SetUpgrade(3, 0);

        SetAffinity_Orange(1);
        SetAffinity_Green(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        PCLActions.Bottom.ChannelOrb(new Fire());
        PCLActions.Bottom.MakeCardInDrawPile(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE);
        PCLActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Random);
        if (info.IsSynergizing) {
            PCLActions.Bottom.ChannelOrb(new Fire());
            PCLActions.Bottom.MakeCardInDrawPile(new Curse_GriefSeed());
        }
    }
}